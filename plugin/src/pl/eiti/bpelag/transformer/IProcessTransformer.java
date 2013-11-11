package pl.eiti.bpelag.transformer;

import org.eclipse.bpel.model.Process;

import pl.eiti.bpelag.model.IModel;

public interface IProcessTransformer {
	public IModel ProcessToModel(Process process);

	public void updateProcessFromModel(Process process, IModel model);
}
