package pl.eiti.bpelag.transformer.impl;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.Process;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.model.IModel;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.transformer.IProcessTransformer;
import pl.eiti.bpelag.util.ActivityUtil;

/**
 * Concrete factory class for producing Graph model from BPEL process, and
 * update BPEL process from Graph model.
 */
public class GraphTransformer implements IProcessTransformer {
	private static GraphTransformer instance = null;

	private GraphTransformer() {
	}

	public static GraphTransformer instance() {
		if (null == instance) {
			instance = new GraphTransformer();
		}
		return instance;
	}

	@Override
	public IModel ProcessToModel(Process process) {
		GraphModel BPELModel = new GraphModel();

		initGraphCreation(process, BPELModel);

		return BPELModel;
	}

	@Override
	public void updateProcessFromModel(Process process, IModel model) {
		// TODO Auto-generated method stub
	}

	private void initGraphCreation(Process process, GraphModel model) {
		TreeIterator<EObject> processIterator = process.eAllContents();
		EObject temp = null;
		while (processIterator.hasNext()) {
			temp = processIterator.next();
			if (temp instanceof Activity) {
				break;
			}
		}
		GraphNode<Activity> rootActivity = new GraphNode<Activity>((Activity) temp);
		GraphNode<Activity> complexNodeClone = null;
		model.setRoot(rootActivity);
		if (!ActivityUtil.isBasicActivity((Activity) temp)) {
			complexNodeClone = new GraphNode<Activity>((Activity) temp);
		}
		createGraph(model.getRoot(), complexNodeClone);
	}

	private void createGraph(GraphNode<Activity> previous, GraphNode<Activity> closingNode) {
		EList<EObject> contents = previous.getData().eContents();
		GraphNode<Activity> insertedNode = null;
		GraphNode<Activity> complexEndNode = null;
		Boolean isFlow = (previous instanceof Flow);

		for (EObject processed : contents) {
			if (processed instanceof Activity) {
				insertedNode = new GraphNode<Activity>((Activity) processed);
				if (ActivityUtil.isBasicActivity((Activity) processed)) {
					insertedNode.addPreviousNode(previous);
					previous.addNextNode(insertedNode);
					if (!isFlow) {
						previous = insertedNode;
					}
				} else {
					complexEndNode = new GraphNode<Activity>((Activity) processed);
					insertedNode.addPreviousNode(previous);
					previous.addNextNode(insertedNode);
					createGraph(insertedNode, complexEndNode);
					if (!isFlow) {
						previous = complexEndNode;
					}
				}
				if (isFlow) {
					insertedNode.addNextNode(closingNode);
					closingNode.addPreviousNode(insertedNode);
				}
			}
		}
		if (!isFlow) {
			previous.addNextNode(closingNode);
			closingNode.addPreviousNode(previous);
		}

	}

}
