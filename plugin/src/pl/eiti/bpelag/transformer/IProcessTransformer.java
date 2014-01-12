package pl.eiti.bpelag.transformer;

import org.eclipse.bpel.model.Process;

import pl.eiti.bpelag.model.IModel;

/**
 * Factory transformer of BPEL Process to model and model to BPEL Process
 * behavior.
 */
public interface IProcessTransformer {
	/**
	 * BPEL process to model declaration.
	 * 
	 * @param process
	 *            process element to transform
	 * @return model created from process
	 */
	public IModel processToModel(Process process);
}
