package pl.eiti.bpelag.analyzer;

public interface IAnalyzer {
	public void init(String pathToBPEL);
	public IAnalysisResult analyze();
}
