package pl.eiti.bpelag.analyzer.impl;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Variables;

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
		result = new AnalysisResult();

		try {
			// TODO implement some magic to conclude assigns between
			// variables
			// TODO get all variables of the process including variables of
			// partner links
			// TODO create set of all variables
			// TODO create set of variables that has value already

			Variables vars = loader.getBPELProcess().getVariables();
			GraphNode<Activity> processedNode = model.getRoot();
			analyzeGraphNodes(processedNode);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			// TODO throw an exception - non initialized Analyzer
			return null;
		}
		return result;
	}

	private GraphNode<Activity> analyzeGraphNodes(GraphNode<Activity> processedNode) {
		GraphNode<Activity> current = processedNode;
		while (current.hasNext() && !current.isVisited()) {
			if (processedNode.hasPrevious() && current.equals(processedNode.getPreviousNodes().get(FIRST))) {
				break;
			}
			Boolean isComplex = ActivityUtil.isBasicActivity(current.getData());
			if (current.isBranched() || isComplex) {
				for (GraphNode<Activity> node : current.getNextNodes()) {
					current = analyzeGraphNodes(node);
				}
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

	/** Accessors section */
	public GraphModel getModel() {
		return model;
	}

	public org.eclipse.bpel.model.Process getBPELProcess() {
		return loader.getBPELProcess();
	}
}
