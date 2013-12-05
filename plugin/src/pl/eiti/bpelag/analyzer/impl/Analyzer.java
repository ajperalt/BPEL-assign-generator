package pl.eiti.bpelag.analyzer.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Invoke;
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

		for (Variable it : processVariables) {
			if (true) { // TODO check if variable has value set
				settedVariables.add(it);
			}
		}
		return null;
	}

	private void analyzeGraphNodes(GraphNode<Activity> rootNode, List<Variable> settedVariables, IAnalysisResult result) {
		GraphNode<Activity> current = rootNode;

		while (current.hasNext() && !current.isVisited()) {
			Activity processingActivity = current.getData();
			Boolean isFlow = ActivityUtil.isFlowActivity(processingActivity);

			if (isFlow) {
				analyzeFlow(current, settedVariables);
				// TODO finish block
			} else if (processingActivity instanceof Assign) {
				List<Invoke> nextInvokes = getNextInvokes(current);
				// TODO try to find variable from set of variables with value
				// that fits invoke request variables with name and type then
				// add to assign element list and after put pair key, value to
				// the result map
			} else {
				if (processingActivity instanceof Invoke) {
					// TODO add reponse variables to setted variables set
				}
				// TODO finish block
			}
			current.setVisited();
			current = current.getNextNodes().get(FIRST);
			current.setProcessing();
		}
	}

	private void analyzeFlow(GraphNode<Activity> flowStartNode, List<Variable> settedVariables) {
		// TODO call analyzeGraphNodes for every parallel path
	}

	private List<Invoke> getNextInvokes(GraphNode<Activity> node) {
		List<Invoke> invokeList = new ArrayList<>();

		// TODO search all next invokes activities and to result list

		return invokeList;
	}

	/** Accessors section */
	public GraphModel getModel() {
		return model;
	}

	public org.eclipse.bpel.model.Process getBPELProcess() {
		return loader.getBPELProcess();
	}
}
