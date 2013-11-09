package transformer;

import model.IModel;

import org.eclipse.bpel.model.Process;

public interface IProcessTransformer {
	public IModel ProcessToModel(Process process);

	public Process ModelToProcess(IModel model);
}
