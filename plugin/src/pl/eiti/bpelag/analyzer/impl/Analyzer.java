package pl.eiti.bpelag.analyzer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Import;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Query;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.Variables;
import org.eclipse.bpel.model.impl.FromImpl;
import org.eclipse.bpel.model.proxy.MessageProxy;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.internal.impl.PartImpl;
import org.eclipse.xsd.XSDElementDeclaration;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.loader.BPELLoader;
import pl.eiti.bpelag.loader.WSDLLoader;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;
import pl.eiti.bpelag.util.ActivityUtil;
import pl.eiti.bpelag.util.Settings;
import pl.eiti.bpelag.util.StringElemUtil;

/**
 * BPEL graph model analyzer class.
 */
public class Analyzer implements IAnalyzer {
	private static final Integer FIRST = Integer.valueOf(0);
	private BPELLoader bpelLoader = null;
	private WSDLLoader wsdlLoader = null;
	private GraphTransformer transformer = null;
	private GraphModel model = null;
	private AnalysisResult result = null;

	/**
	 * Default constructor.
	 */
	public Analyzer() {
		this.bpelLoader = new BPELLoader();
		this.wsdlLoader = new WSDLLoader();
		transformer = GraphTransformer.instance();
		this.model = new GraphModel();
	}

	@Override
	public void init(String pathToBPEL) {
		bpelLoader.setBPELFileLocation(pathToBPEL);
		bpelLoader.loadProcess();

		List<String> importLocation = new ArrayList<>();
		for (Import importElement : bpelLoader.getBPELProcess().getImports()) {
			importLocation.add(importElement.getLocation());
		}

		if (!importLocation.isEmpty()) {
			wsdlLoader.load(importLocation, StringElemUtil.getPath(pathToBPEL));
		}

		model = (GraphModel) transformer.ProcessToModel(bpelLoader.getBPELProcess());
	}

	@Override
	public IAnalysisResult analyze() {
		List<Variable> processVariables = getProcVariables(bpelLoader.getBPELProcess().getVariables());
		List<Variable> settedVariables = getSettedVariables(processVariables);

		result = new AnalysisResult();

		try {
			GraphNode<Activity> processedNode = model.getRoot();
			analyzeGraphNodes(processedNode, settedVariables, result);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			// TODO throw an exception - non initialized Analyzer
			return null;
		}
		return result;
	}

	/**
	 * Gets all process variables list from given EObjects set Variables
	 * 
	 * @param variables
	 *            EObject variables
	 * @return list all process variables (type of Variable)
	 */
	private List<Variable> getProcVariables(Variables variables) {
		List<Variable> procVariables = new ArrayList<>();

		for (EObject it : variables.eContents()) {
			if (it instanceof Variable) {
				procVariables.add((Variable) it);
			}
		}

		return procVariables;
	}

	/**
	 * Gets list of process variables with value already set from all process
	 * variables list.
	 * 
	 * @param processVariables
	 *            all process variables list
	 * @return process variables list with value set
	 */
	private List<Variable> getSettedVariables(List<Variable> processVariables) {
		List<Variable> settedVariables = new ArrayList<>();

		for (Receive it : bpelLoader.getAllReceives()) {
			settedVariables.add(it.getVariable());
		}

		for (Variable it : processVariables) {
			if (null != it.getFrom() && null != it.getFrom().getLiteral()) {
				settedVariables.add(it);
			}
		}
		return settedVariables;
	}

	/**
	 * Analyzes path of graph and retrieves possibilities to create assign
	 * activities.
	 * 
	 * @param rootNode
	 *            analyze start node of graph
	 * @param settedVariables
	 *            list of process variables with value set
	 * @param result
	 *            result of analysis as list of assign block mapped to copy
	 *            elements to add to assigns
	 */
	private void analyzeGraphNodes(GraphNode<Activity> rootNode, List<Variable> settedVariables,
			IAnalysisResult analysisResult) {
		GraphNode<Activity> current = rootNode;

		while (current.hasNext() && !current.isVisited()) {
			Activity processingActivity = current.getData();
			Boolean isFlow = ActivityUtil.isFlowActivity(processingActivity);

			if (isFlow) {
				if (rootNode.hasPrevious()
						&& processingActivity.equals(rootNode.getPreviousNodes().get(FIRST).getData())) {
					break;
				} else {
					analyzeFlow(current, settedVariables, analysisResult);
				}
			} else if (processingActivity instanceof Assign) {
				List<Invoke> followingInvokes = new ArrayList<>();
				getNextInvokes(current.getNextNodes().get(FIRST), followingInvokes, Boolean.FALSE);

				List<Copy> copyBlocks = createCopyForMatchedVariables(settedVariables, followingInvokes);

				if (!(null == copyBlocks || copyBlocks.isEmpty())) {
					analysisResult.put((Assign) processingActivity, copyBlocks);
				}
			} else {
				if (processingActivity instanceof Invoke) {
					Invoke invokeActivity = (Invoke) processingActivity;
					if (null != invokeActivity) {
						settedVariables.add(invokeActivity.getOutputVariable());
					}
				}
			}
			current.setVisited();
			current = current.getNextNodes().get(FIRST);
			current.setProcessing();
		}
	}

	/**
	 * Finds all variables from given list that match (with name and type) to
	 * any input variables of invokes from given list.
	 * 
	 * @param settedVariables
	 *            list of variables that can be used as invoke call parameters
	 * @param followingInvokes
	 *            list of invoke activities following currently processed assign
	 *            activity
	 */
	private List<Copy> createCopyForMatchedVariables(List<Variable> settedVariables, List<Invoke> followingInvokes) {
		List<Copy> result = new ArrayList<>();

		for (Invoke it : followingInvokes) {
			Variable invokeInput = it.getInputVariable();

			for (Variable var : settedVariables) {

				if (null != var.getMessageType() && null != invokeInput.getMessageType()) {
					// both types complex

					if (var.getMessageType().equals(invokeInput.getMessageType())) {
						// create and add copy instruction here
						From fromElement = BPELFactory.eINSTANCE.createFrom();
						To toElement = BPELFactory.eINSTANCE.createTo();
						fromElement.setVariable(var);
						toElement.setVariable(invokeInput);

						Copy copyElement = BPELFactory.eINSTANCE.createCopy();
						copyElement.setFrom(fromElement);
						copyElement.setTo(toElement);

						result.add(copyElement);
					} else {
						result.addAll(retrieveComplexTypeMatching(var, invokeInput));
					}

					// copy instruction created - leave the loop
					break;
				} else if (null != var.getMessageType() && null == invokeInput.getMessageType()) {
					// complex variable but invoke input simple type

					result.addAll(retrieveSimpleToComplexTypeMatching(invokeInput, var, Boolean.FALSE));
					// copy instruction created - leave the loop
					break;
				} else if (null == var.getMessageType() && null != invokeInput.getMessageType()) {
					// simple type variable but invoke input complex type

					result.addAll(retrieveSimpleToComplexTypeMatching(var, invokeInput, Boolean.TRUE));
					// copy instruction created - leave the loop
					break;
				} else {
					// both types simple
					if (isMatching(invokeInput, var)) {
						// create and add copy instruction here
						From fromElement = BPELFactory.eINSTANCE.createFrom();
						To toElement = BPELFactory.eINSTANCE.createTo();
						fromElement.setVariable(var);
						toElement.setVariable(invokeInput);

						Copy copyElement = BPELFactory.eINSTANCE.createCopy();
						copyElement.setFrom(fromElement);
						copyElement.setTo(toElement);

						result.add(copyElement);
					}
					// copy instruction created - leave the loop
					break;
				}
			}
		}

		return result;
	}

	/**
	 * Retrieves all matchings between simple types that variables given as
	 * parameters includes.
	 * 
	 * @param varToCopyFrom
	 *            complex type variable to copy from
	 * @param varToCopyTo
	 *            complex type variable to copy to
	 * @return list of copy elements
	 */
	private List<Copy> retrieveComplexTypeMatching(Variable varToCopyFrom, Variable varToCopyTo) {
		List<Copy> result = new ArrayList<>();

		Message varComplexType = wsdlLoader.getMessage(varToCopyFrom.getMessageType().getQName());
		Message invokeComplexType = wsdlLoader.getMessage(varToCopyTo.getMessageType().getQName());

		List<String> varMessageElements = retrieveElements(varComplexType);
		List<String> invokeMessageElements = retrieveElements(invokeComplexType);

		List<SimpleMessageElement> simpleElements = new ArrayList<>();

		for (String varMessageElem : varMessageElements) {
			String[] varMessageInfo = varMessageElem.split(Settings.M_DELIMITER);

			for (String invokeMessageElem : invokeMessageElements) {
				String[] invokeMessageInfo = invokeMessageElem.split(Settings.M_DELIMITER);

				if (invokeMessageInfo[1].equals(varMessageInfo[1]) && invokeMessageInfo[2].equals(varMessageInfo[2])) {
					SimpleMessageElement simpleElem = new SimpleMessageElement();
					simpleElem.fromSimpleMessage = varMessageElem;
					simpleElem.toSimpleMessage = invokeMessageElem;

					simpleElements.add(simpleElem);
				}
			}
		}

		for (SimpleMessageElement match : simpleElements) {

			if (null != match.fromSimpleMessage && null != match.toSimpleMessage) {
				String[] fromElementSplitted = match.fromSimpleMessage.split(Settings.M_DELIMITER);
				String[] toElementSplitted = match.toSimpleMessage.split(Settings.M_DELIMITER);

				Query fromQuery = BPELFactory.eINSTANCE.createQuery();
				fromQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
				fromQuery.setValue(((MessageProxy) varToCopyFrom.getMessageType()).getQName().getPrefix() + ":"
						+ fromElementSplitted[1]);

				Query toQuery = BPELFactory.eINSTANCE.createQuery();
				toQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
				toQuery.setValue(((MessageProxy) varToCopyTo.getMessageType()).getQName().getPrefix() + ":"
						+ toElementSplitted[1]);

				From fromElement = BPELFactory.eINSTANCE.createFrom();
				To toElement = BPELFactory.eINSTANCE.createTo();

				fromElement.setVariable(varToCopyFrom);
				if (fromElement instanceof FromImpl) {
					((FromImpl) fromElement).setPartName(fromElementSplitted[0]);
				}
				fromElement.setQuery(fromQuery);

				toElement.setVariable(varToCopyTo);
				if (toElement instanceof FromImpl) {
					((FromImpl) toElement).setPartName(toElementSplitted[0]);
				}
				toElement.setQuery(toQuery);

				Copy newCopy = BPELFactory.eINSTANCE.createCopy();
				newCopy.setFrom(fromElement);
				newCopy.setTo(toElement);
				result.add(newCopy);
			}
		}

		return result;
	}

	/**
	 * Retrieves all matchings between simple type variable given as parameter
	 * and complex type elements variable with additional switch that inform if
	 * it will be copy from simple to complex type or from complex to simple
	 * type.
	 * 
	 * @param simpleTypeVar
	 *            simple type variable
	 * @param complexTypeVar
	 *            complex type variable
	 * @param fromSimpleToComplex
	 *            switch that determines copy instruction direction
	 * @return list of copy elements
	 */
	private List<Copy> retrieveSimpleToComplexTypeMatching(Variable simpleTypeVar, Variable complexTypeVar,
			Boolean fromSimpleToComplex) {
		List<Copy> result = new ArrayList<>();

		Message complexType = wsdlLoader.getMessage(complexTypeVar.getMessageType().getQName());

		List<String> messageElements = retrieveElements(complexType);

		String simpleElement = null;

		for (String messageElem : messageElements) {
			String[] messageInfo = messageElem.split(Settings.M_DELIMITER);

			if (simpleTypeVar.getName().equals(messageInfo[1])
					&& simpleTypeVar.getType().getName().equals(messageInfo[2])) {
				simpleElement = messageElem;
				break;
			}
		}

		if (null != simpleElement) {
			String[] elementSplitted = simpleElement.split(Settings.M_DELIMITER);

			Query query = BPELFactory.eINSTANCE.createQuery();
			query.setQueryLanguage(Settings.QUERY_LANGUAGE);
			query.setValue(((MessageProxy) complexTypeVar.getMessageType()).getQName().getPrefix() + ":"
					+ elementSplitted[1]);

			From fromElement = BPELFactory.eINSTANCE.createFrom();
			To toElement = BPELFactory.eINSTANCE.createTo();

			if (fromSimpleToComplex) {
				fromElement.setVariable(simpleTypeVar);
				toElement.setVariable(complexTypeVar);
				if (toElement instanceof FromImpl) {
					((FromImpl) toElement).setPartName(elementSplitted[0]);
				}
				toElement.setQuery(query);
			} else {
				toElement.setVariable(simpleTypeVar);
				fromElement.setVariable(complexTypeVar);
				if (fromElement instanceof FromImpl) {
					((FromImpl) fromElement).setPartName(elementSplitted[0]);
				}
				fromElement.setQuery(query);
			}

			Copy newCopy = BPELFactory.eINSTANCE.createCopy();
			newCopy.setFrom(fromElement);
			newCopy.setTo(toElement);
			result.add(newCopy);
		}

		return result;
	}

	/**
	 * Checks if simple type variables given as parameters are matching.
	 * 
	 * @param varTo
	 *            simple type variable to copy to
	 * @param varFrom
	 *            simple type variable to copy from
	 * @return boolean type information if parameters are matching
	 */
	private Boolean isMatching(Variable varTo, Variable varFrom) {
		Boolean result = Boolean.FALSE;

		if (varFrom.getName().equals(varTo.getName())
				&& ((null != varFrom.getMessageType() && varFrom.getMessageType().equals(varTo.getMessageType())) || (null != varFrom
						.getType() && varFrom.getType().equals(varTo.getType())))) {
			result = Boolean.TRUE;
		}
		return result;
	}

	/**
	 * Retrieves all elements of given complex type message and create
	 * concatenated information about them delimited by M_LIMITER defined in
	 * Settings class
	 * (MessagePartName/delimiter/xsdElementTypeName/delimiter/xsdElementName).
	 * 
	 * @param complexType
	 *            defined in wsdl complex type message
	 * @return list of concatenated information about elements
	 */
	private List<String> retrieveElements(Message complexType) {
		List<String> result = new ArrayList<>();

		if (complexType instanceof Message) {
			Map<String, PartImpl> partsMap = ((Message) complexType).getParts();

			for (String partName : partsMap.keySet()) {
				PartImpl elementPart = (PartImpl) ((Message) complexType).getParts().get(partName);

				if (null != elementPart) {
					TreeIterator<EObject> iterator = elementPart.getElementDeclaration().eAllContents();

					while (iterator.hasNext()) {
						EObject object = (EObject) iterator.next();

						if (object instanceof XSDElementDeclaration) {
							XSDElementDeclaration xsdED = (XSDElementDeclaration) object;
							result.add(partName + Settings.M_LIMITER + xsdED.getType().getName() + Settings.M_LIMITER
									+ xsdED.getName());
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Analyzes parallel paths in graph started from flow node.
	 * 
	 * @param flowStartNode
	 *            node of flow type, has several next nodes
	 * @param settedVariables
	 *            list of process variables with value set
	 * @param analysisResult
	 *            result of analysis process graph
	 */
	private void analyzeFlow(GraphNode<Activity> flowStartNode, List<Variable> settedVariables,
			IAnalysisResult analysisResult) {
		for (GraphNode<Activity> node : flowStartNode.getNextNodes()) {
			analyzeGraphNodes(node, settedVariables, analysisResult);
		}
	}

	/**
	 * Retrieves graph searching all of invoke type nodes following node given
	 * as parameter.
	 * 
	 * @param node
	 *            currently processing node
	 * @param invokeList
	 *            list of node following invoke activities
	 * @return list of following node type of invoke
	 */
	private List<Invoke> getNextInvokes(GraphNode<Activity> node, List<Invoke> invokeList, Boolean isAssignFound) {
		GraphNode<Activity> tempNode = node;

		while (tempNode.hasNext()) {
			Activity tempActivity = tempNode.getData();

			if (tempNode.isBranched()) {
				for (GraphNode<Activity> it : tempNode.getNextNodes()) {
					getNextInvokes(it, invokeList, isAssignFound);
				}
				if (isAssignFound) {
					break;
				}
			}

			if (tempActivity instanceof Invoke && !invokeList.contains((Invoke) tempActivity)) {
				invokeList.add((Invoke) tempActivity);
				break;
			} else if (tempActivity instanceof Assign) {
				isAssignFound = Boolean.TRUE;
				break;
			}
			tempNode = tempNode.getNextNodes().get(FIRST);
		}

		return invokeList;
	}

	@Override
	public List<Assign> getAssignActivities() {
		return bpelLoader.getAllAssignBlocks();
	}

	@Override
	public List<Variable> getProcessVariables() {
		return bpelLoader.getAllVariables();
	}

	/** Accessor section */
	public GraphModel getModel() {
		return model;
	}

	public org.eclipse.bpel.model.Process getBPELProcess() {
		return bpelLoader.getBPELProcess();
	}

	@Override
	public Copy createCopy() {
		return BPELFactory.eINSTANCE.createCopy();
	}

	private class SimpleMessageElement {
		public String fromSimpleMessage;
		public String toSimpleMessage;
	}
}
