package pl.eiti.bpelag.model.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.emf.ecore.EObject;

public class ActivitySet extends HashSet<Activity> {

	private static final long serialVersionUID = -4273227675879688795L;

	public void createProcessActivity(BPELExtensibleElement element) {
		this.addAll(findAllActivities(element));
	}

	private static Set<Activity> findAllActivities(BPELExtensibleElement element) {
		Set<Activity> allActs = new HashSet<Activity>();
		if (element instanceof Activity) {
			allActs.add((Activity) element);
		}
		for (EObject object : element.eContents()) {
			if (!(object instanceof BPELExtensibleElement)) {
				continue;
			}
			BPELExtensibleElement contentsElement = (BPELExtensibleElement) object;
			Set<Activity> allSubActs = findAllActivities(contentsElement);
			allActs.addAll(allSubActs);
		}
		return allActs;
	}
}
