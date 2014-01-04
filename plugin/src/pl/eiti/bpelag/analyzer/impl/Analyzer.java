package pl.eiti.bpelag.analyzer.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Import;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.Variables;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.loader.BPELLoader;
import pl.eiti.bpelag.loader.WSDLLoader;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;
import pl.eiti.bpelag.util.ActivityUtil;
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

		// This is how to create new copy element
		Copy copy = BPELFactory.eINSTANCE.createCopy();
		// Thanks
		Map<String, List<String>> settedVarsElementsMap = new HashMap<>();

		for (Variable setVar : settedVariables) {
			String processingVar = setVar.getName();
			settedVarsElementsMap.put(processingVar, new ArrayList<String>());

			if (null != setVar.getMessageType()) {
				// complex type variable
				settedVarsElementsMap.get(processingVar).addAll(retrieveElements(setVar));
			}
		}

		List<String> invokeInputElements = null;

		for (Invoke it : followingInvokes) {
			Variable invokeInput = it.getInputVariable();
			// TODO here create list of primitive type variables that are part
			// of complex invoke input type
			if (null != invokeInput.getMessageType()) {
				invokeInputElements = retrieveElements(invokeInput);
			}
		}

		return result;
	}

	private List<String> retrieveElements(Variable invokeInput) {
		// TODO Auto-generated method stub
		return null;
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
}
