package pl.eiti.bpelag.transformer.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.Process;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.model.IModel;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.transformer.IProcessTransformer;

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

		// TODO put here some magic to create graph model of the process

		return BPELModel;
	}

	@Override
	public Process ModelToProcess(IModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	// private static Set<Activity> A(BPELExtensibleElement element) {
	//
	// }
}
