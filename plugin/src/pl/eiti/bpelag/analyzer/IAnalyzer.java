package pl.eiti.bpelag.analyzer;

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
}
