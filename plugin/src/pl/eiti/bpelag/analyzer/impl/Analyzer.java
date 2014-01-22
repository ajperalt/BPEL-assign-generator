package pl.eiti.bpelag.analyzer.impl;

import java.util.ArrayList;
import java.util.List;

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
import pl.eiti.bpelag.dao.BPELDAO;
import pl.eiti.bpelag.dao.WSDLDAO;
import pl.eiti.bpelag.matcher.IMatcher;
import pl.eiti.bpelag.matcher.impl.DefaultMatcher;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;
import pl.eiti.bpelag.util.ActivityUtil;
import pl.eiti.bpelag.util.Settings;
import pl.eiti.bpelag.util.StringElemUtil;

/**
 * BPEL graph model analyzer class.
 */
public class Analyzer extends Settings implements IAnalyzer {
	private static final Integer FIRST = Integer.valueOf(0);
	private BPELDAO bpelLoader = null;
	private WSDLDAO wsdlLoader = null;
	private GraphTransformer transformer = null;
	private GraphModel model = null;
	private IAnalysisResult<Assign, List<Copy>> result = null;
	private IMatcher matcher = null;

	/**
	 * Default constructor.
	 */
	public Analyzer() {
		this.bpelLoader = new BPELDAO();
		this.wsdlLoader = WSDLDAO.getInstance();
		transformer = GraphTransformer.getInstance();
		this.model = new GraphModel();
		this.matcher = new DefaultMatcher();
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

		model = (GraphModel) transformer.processToModel(bpelLoader.getBPELProcess());
	}

	@Override
	public IAnalysisResult<Assign, List<Copy>> analyze() {
		List<Variable> processVariables = getProcVariables(bpelLoader.getBPELProcess().getVariables());
		List<Variable> settedVariables = getSettedVariables(processVariables);

		result = new AnalysisResult();

		try {
			GraphNode<Activity> processingNode = model.getRoot();
			analyzeGraphNodes(processingNode, settedVariables, result);
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
			IAnalysisResult<Assign, List<Copy>> analysisResult) {
		GraphNode<Activity> current = rootNode;
		System.out.println();
		while (current.hasNext() && !current.isVisited()) {
			Activity processingActivity = current.getData();
			Boolean isFlow = ActivityUtil.isFlowActivity(processingActivity);

			if (isFlow) {
				if (rootNode.hasPrevious()
						&& processingActivity.equals(rootNode.getPreviousNodes().get(FIRST).getData())) {
					break;
				} else {
					analyzeFlow(current, settedVariables, analysisResult);
					current.setVisited();
					current = getClosingFlow(current);
				}
			} else if (processingActivity instanceof Assign) {
				List<Invoke> followingInvokes = new ArrayList<>();
				getNextInvokes(current.getNextNodes().get(FIRST), followingInvokes, Boolean.FALSE);

				List<Copy> copyBlocks = matcher.createCopyForMatchedVariables(settedVariables, followingInvokes);

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
			if (current.isUnvisited()) {
				current.setProcessing();
			}
		}
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
			IAnalysisResult<Assign, List<Copy>> analysisResult) {
		for (GraphNode<Activity> node : flowStartNode.getNextNodes()) {
			analyzeGraphNodes(node, settedVariables, analysisResult);
		}
	}

	private GraphNode<Activity> getClosingFlow(GraphNode<Activity> openFlow) {
		GraphNode<Activity> result = null;
		GraphNode<Activity> iter = openFlow;

		while (iter.hasNext()) {
			iter = iter.getNextNodes().get(FIRST);
			if (iter.getData().equals(openFlow.getData())) {
				result = iter;
			}
		}

		return result;
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
				// break;
			} else if (tempActivity instanceof Assign) {
				isAssignFound = Boolean.TRUE;
				break;
			}
			tempNode = tempNode.getNextNodes().get(FIRST);
		}

		return invokeList;
	}

	public void saveProcess() {
		this.bpelLoader.saveProcess();
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
