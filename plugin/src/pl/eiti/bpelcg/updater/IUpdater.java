package pl.eiti.bpelcg.updater;

import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Query;
import org.eclipse.bpel.model.To;

import pl.eiti.bpelcg.analyzer.IAnalysisResult;

/**
 * Behavior of updater BPEL process elements using elements from analysis
 * result.
 */
public interface IUpdater {
	/**
	 * Updates BPEL process using analysis result elements.
	 * 
	 * @param BPELprocess
	 *            BPEL process in memory.
	 * @param analysis
	 *            result of process analyze.
	 */
	public void update(org.eclipse.bpel.model.Process BPELprocess, @SuppressWarnings("rawtypes") IAnalysisResult analysis);

	/**
	 * Creates a new From element - source of value copying in copy instruction
	 * from assign BPEL element.
	 * 
	 * @return instance of From element.
	 */
	public From createNewFrom();

	/**
	 * Creates a new To element - destination of value copying in copy
	 * instruction from assign BPEL element.
	 * 
	 * @return instance of To element.
	 */
	public To createNewTo();

	/**
	 * Creates a query used to retrieve value of field of complex type variable
	 * in BPEL copy instruction.
	 * 
	 * @return instance of Query element.
	 */
	public Query createNewQuery();
}
