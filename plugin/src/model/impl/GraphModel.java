package model.impl;

import model.IModel;

import org.eclipse.bpel.model.Process;

public class GraphModel implements IModel {
	private ActivitySet activities = null;

	public GraphModel() {
		this.activities = new ActivitySet();
	}

	public GraphModel(Process newProcess) {
		this();
		this.activities.createProcessActivity(newProcess);
	}

	/** Accessors section */
	public ActivitySet getActivities() {
		return this.activities;
	}
}
