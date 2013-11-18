package pl.eiti.bpelag.controller;

import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.analyzer.impl.Analyzer;
import pl.eiti.bpelag.generator.IGenerator;
import pl.eiti.bpelag.generator.impl.Generator;

public class AnalyzerWizardController {
	private IAnalyzer analyzer = null;
	private IGenerator generator = null;

	private String pathToBPEL = null;

	public AnalyzerWizardController(final String pathToFile) {
		pathToBPEL = pathToFile;
		analyzer = new Analyzer();
		generator = new Generator();

		analyzer.init(pathToBPEL);
	}

	public void executeAnalyze() {
		
	}

}
