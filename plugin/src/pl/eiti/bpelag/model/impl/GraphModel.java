package pl.eiti.bpelag.model.impl;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;

import pl.eiti.bpelag.model.IModel;
import pl.eiti.bpelag.model.graph.Graph;

public class GraphModel extends Graph<Activity> implements IModel {
//	private ActivitySet activities = null;

	public GraphModel() {
//		this.activities = new ActivitySet();
	}

	public GraphModel(Process newProcess) {
		this();
//		this.activities.createProcessActivity(newProcess);
	}

	/** Accessors section */
//	public ActivitySet getActivities() {
//		return this.activities;
//	}
}
