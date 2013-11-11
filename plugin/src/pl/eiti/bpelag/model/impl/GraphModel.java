package pl.eiti.bpelag.model.impl;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;

import pl.eiti.bpelag.model.IModel;
import pl.eiti.bpelag.model.graph.Graph;

public class GraphModel extends Graph<Activity> implements IModel {

	public GraphModel() {
		super();
	}

	public GraphModel(Process newProcess) {
		this();
	}
}
