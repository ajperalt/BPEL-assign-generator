package pl.eiti.bpelag.temp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Variable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.util.ActivityUtil;

public class temp {
	
	createGraphModel(process, model) {
		processIterator = process.eAllContents();
		temp = null;
		while (processIterator.hasNext()) {
			temp = processIterator.next();
			if (temp instanceof Activity) {
				break;
			}
		}
		node rootActivity = new node(temp);
		node complexNodeClone = null;
		model.root := rootActivity;
		if (temp not basic activity) {
			complexNodeClone = new node(temp);
		}
		
		executeCreate(model.root, complexNodeClone);
	}

	
	executeCreate(startNode, closingNode) {
		contents := startNode.activityChildren;
		insertedNode = null;
		complexEndNode = null;
		bool isFlow = (startNode.activity instanceof Flow);

		for (processing : contents) {
			if (processing instanceof Activity) {
				insertedNode = new node(processed);
				if (processed is basic activity) {
					insertedNode.previouseList.add(previous);
					previous.nextList.add(insertedNode);
					if (isFlow) {
						insertedNode.nextList.add(closingNode);
						closingNode.previousList.add(insertedNode);
					} else {
						previous = insertedNode;
					}
				} else {
					complexEndNode = new node(processed);
					insertedNode.previousList.add(previous);
					previous.nextList.add(insertedNode);
					
					executeCreate(insertedNode, complexEndNode);
					
					if (isFlow) {
						complexEndNode.addNextNode(closingNode);
						closingNode.addPreviousNode(complexEndNode);
					} else {
						previous = complexEndNode;
					}
				}
			}
		}
		if (!isFlow) {
			previous.nextList.add(closingNode);
			closingNode.previousList.add(previous);
		}
	}
	
	
	
	
	
	
	
	/////////////////////////////////////////////Analyzer algorithm///////////////////
	
	
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
}
