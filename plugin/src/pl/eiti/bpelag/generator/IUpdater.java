package pl.eiti.bpelag.generator;

import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Query;
import org.eclipse.bpel.model.To;

import pl.eiti.bpelag.analyzer.IAnalysisResult;

public interface IUpdater {
	public void update(org.eclipse.bpel.model.Process BPELprocess, IAnalysisResult analysis);

	public From createNewFrom();

	public To createNewTo();

	public Query createNewQuery();
}
