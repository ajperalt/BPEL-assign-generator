package pl.eiti.bpelag.model.impl;

import org.eclipse.bpel.model.Activity;

import pl.eiti.bpelag.model.IModel;
import pl.eiti.bpelag.model.graph.Graph;

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
