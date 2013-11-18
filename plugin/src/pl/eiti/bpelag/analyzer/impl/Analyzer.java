package pl.eiti.bpelag.analyzer.impl;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.reader.BPELReader;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;

public class Analyzer implements IAnalyzer {
	private BPELReader loader = null;
	private GraphTransformer transformer = null;
	private GraphModel model = null;
	private AnalysisResult result = null;

	public Analyzer() {
		this.loader = new BPELReader();
		transformer = GraphTransformer.instance();
		this.model = new GraphModel();
	}

	@Override
	public void init(String pathToBPEL) {
		loader.setBPELFileLocation(pathToBPEL);
		loader.loadProcess();
		model = (GraphModel) transformer.ProcessToModel(loader.getBPELProcess());
	}

	@Override
	public IAnalysisResult analyze() {
		result = new AnalysisResult();

		try {
			// TODO implement some magic to conclude assigns between
			// variables
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			// TODO throw an exception - non initialized Analyzer
			return null;
		}
		return result;
	}

}
