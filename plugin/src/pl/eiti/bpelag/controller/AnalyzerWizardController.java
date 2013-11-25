package pl.eiti.bpelag.controller;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.analyzer.impl.Analyzer;
import pl.eiti.bpelag.generator.IGenerator;
import pl.eiti.bpelag.generator.impl.Generator;

public class AnalyzerWizardController {
	private IAnalyzer analyzer = null;
	private IGenerator generator = null;

	private String pathToBPEL = null;
	private IAnalysisResult analysisResult = null;

	public AnalyzerWizardController() {
		analyzer = new Analyzer();
		generator = new Generator();
	}

	public AnalyzerWizardController(final String pathToFile) {
		this();
		pathToBPEL = pathToFile;
		analyzer.init(pathToBPEL);
		executeAnalyze();
	}

	public void executeAnalyze() {
		analysisResult = analyzer.analyze();
	}

	public void executeGenerator() {
		if (analyzer instanceof Analyzer) {
			generator.generate(((Analyzer) analyzer).getBPELProcess(), analysisResult);
		}
	}

}
