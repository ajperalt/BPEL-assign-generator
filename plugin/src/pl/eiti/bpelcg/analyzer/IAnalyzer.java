package pl.eiti.bpelcg.analyzer;

import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Variable;

/**
 * Behavior of analyzer for model created from BPEL process.
 */
public interface IAnalyzer {
	/**
	 * Initial method for analyzer to prepare necessary things (load BPEL,
	 * WSDLs) to analyze.
	 * 
	 * @param pathToBPEL
	 *            BPEL process file location
	 */
	public void init(String pathToBPEL);

	/**
	 * Analyze existing loaded model and give results of the analyze back.
	 * 
	 * @return results of the model analyze
	 */
	@SuppressWarnings("rawtypes")
	public IAnalysisResult analyze();

	/**
	 * Gets all assign blocks from BPEL process.
	 * 
	 * @return list of assign block of BPEL process
	 */
	public List<Assign> getAssignActivities();

	/**
	 * Gets all variables of analyzed BPEL process.
	 * 
	 * @return list of variables of BPEL process
	 */
	public List<Variable> getProcessVariables();

	public Copy createCopy();
}
