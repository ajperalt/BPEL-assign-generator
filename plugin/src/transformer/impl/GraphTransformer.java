package transformer.impl;

import model.IModel;
import model.impl.GraphModel;

import org.eclipse.bpel.model.Process;

import transformer.IProcessTransformer;

public class GraphTransformer implements IProcessTransformer {

	@Override
	public IModel ProcessToModel(Process process) {
		GraphModel BPELModel = new GraphModel();
		
		return BPELModel;
	}

	@Override
	public Process ModelToProcess(IModel model) {
		// TODO Auto-generated method stub
		return null;
	}
}
