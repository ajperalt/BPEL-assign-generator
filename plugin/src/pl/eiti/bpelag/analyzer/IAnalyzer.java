package pl.eiti.bpelag.analyzer;

import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Variable;

/**
 * Model created from BPEL process analyzer behavior.
 */
public interface IAnalyzer {
	/**
	 * Initial method for analyzer to prepare necessary thing to analyze.
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
