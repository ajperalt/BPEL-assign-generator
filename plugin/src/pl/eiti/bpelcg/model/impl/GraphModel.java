package pl.eiti.bpelcg.model.impl;

import org.eclipse.bpel.model.Activity;

import pl.eiti.bpelcg.model.IModel;
import pl.eiti.bpelcg.model.graph.Graph;

/**
 * Graph model with nodes org.eclipse.bpel.model.Activity type.
 */
public class GraphModel extends Graph<Activity> implements IModel {

	/**
	 * Default graph model constructor.
	 */
	public GraphModel() {
		super();
	}
}
