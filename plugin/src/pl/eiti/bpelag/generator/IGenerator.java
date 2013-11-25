package pl.eiti.bpelag.generator;

import pl.eiti.bpelag.analyzer.IAnalysisResult;

public interface IGenerator {
	public void generate(org.eclipse.bpel.model.Process BPELprocess, IAnalysisResult analysis);
}
