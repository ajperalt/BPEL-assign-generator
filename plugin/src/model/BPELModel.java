package model;

import org.eclipse.bpel.model.Process;

public class BPELModel {
	private ActivitySet activities = null;

	public BPELModel() {
		this.activities = new ActivitySet();
	}

	public BPELModel(Process newProcess) {
		this();
		this.activities.createProcessActivity(newProcess);
	}

	/** Accessors section */
	public ActivitySet getActivities() {
		return this.activities;
	}
}
