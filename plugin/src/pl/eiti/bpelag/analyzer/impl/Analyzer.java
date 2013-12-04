package pl.eiti.bpelag.analyzer.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.Variables;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.reader.BPELReader;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;
import pl.eiti.bpelag.util.ActivityUtil;

/**
 * BPEL graph model analyzer class.
 */
public class Analyzer implements IAnalyzer {
	private static final Integer FIRST = Integer.valueOf(0);
	private BPELReader loader = null;
	private GraphTransformer transformer = null;
	private GraphModel model = null;
	private AnalysisResult result = null;

	/**
	 * Default constructor.
	 */
	public Analyzer() {
		this.loader = new BPELReader();
		transformer = GraphTransformer.instance();
		this.model = new GraphModel();
	}

	@Override
	public void init(String pathToBPEL) {
		loader.setBPELFileLocation(pathToBPEL);
		loader.loadProcess();
		model = (GraphModel) transformer.ProcessToModel(loader.getBPELProcess());
	}

	@Override
	public IAnalysisResult analyze() {
		List<Variable> processVariables = getProcVariables(loader.getBPELProcess().getVariables());
		List<Variable> settedVariables = getSettedVariables(processVariables);

		result = new AnalysisResult();

		try {
			GraphNode<Activity> processedNode = model.getRoot();
			analyzeGraphNodes(processedNode, settedVariables);
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

		for (Variable it : processVariables) {
			if (true) { // TODO check if variable has value set
				settedVariables.add(it);
			}
		}
		return null;
	}

	private GraphNode<Activity> analyzeGraphNodes(GraphNode<Activity> rootNode, List<Variable> settedVariables) {
		GraphNode<Activity> current = rootNode;

		while (current.hasNext() && !current.isVisited()) {
			Boolean isFlow = ActivityUtil.isFlowActivity(current.getData());

			if (isFlow) {
				analyzeFlow(current, settedVariables);
				// TODO finish block
			} else {
				current.setVisited();
				current = current.getNextNodes().get(FIRST);
				current.setProcessed();
				// TODO finish block
			}
		}
		return current;
	}

	// private GraphNode<Activity> analyzeGraphNodes(GraphNode<Activity>
	// processedNode) {
	// GraphNode<Activity> current = processedNode;
	// while (current.hasNext() && !current.isVisited()) {
	// if (processedNode.hasPrevious() &&
	// current.equals(processedNode.getPreviousNodes().get(FIRST))) {
	// break;
	// }
	// Boolean isComplex = ActivityUtil.isBasicActivity(current.getData());
	// if (current.isBranched() || isComplex) {
	// for (GraphNode<Activity> node : current.getNextNodes()) {
	// current = analyzeGraphNodes(node);
	// }
	// // TODO finish block
	// } else {
	// current.setVisited();
	// current = current.getNextNodes().get(FIRST);
	// current.setProcessed();
	// // TODO finish block
	// }
	// }
	// return current;
	// }

	private void analyzeFlow(GraphNode<Activity> flowStartNode, List<Variable> settedVariables) {

	}

	/** Accessors section */
	public GraphModel getModel() {
		return model;
	}

	public org.eclipse.bpel.model.Process getBPELProcess() {
		return loader.getBPELProcess();
	}
}
