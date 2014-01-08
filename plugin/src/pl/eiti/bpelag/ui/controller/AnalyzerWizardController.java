package pl.eiti.bpelag.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.wst.wsdl.Message;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.analyzer.impl.Analyzer;
import pl.eiti.bpelag.generator.IGenerator;
import pl.eiti.bpelag.generator.impl.Generator;
import pl.eiti.bpelag.ui.model.AnalyzerWizardModel;

public class AnalyzerWizardController {
	private IAnalyzer analyzer = null;
	private IGenerator generator = null;

	private String pathToBPEL = null;
	private IAnalysisResult analysisResult = null;

	private AnalyzerWizardModel model = null;

	public AnalyzerWizardController() {
		analyzer = new Analyzer();
		generator = new Generator();
	}

	public AnalyzerWizardController(final String pathToFile, AnalyzerWizardModel newModel) {
		this();
		pathToBPEL = pathToFile;
		model = newModel;
		analyzer.init(pathToBPEL);
		executeAnalyze();
		executeGenerator();
	}

	public void executeAnalyze() {
		analysisResult = analyzer.analyze();
		model.setAssignList(analyzer.getAssignActivities());
		model.setProcessVariables(analyzer.getProcessVariables());
	}

	public void executeGenerator() {
		if (analyzer instanceof Analyzer) {
			generator.generate(((Analyzer) analyzer).getBPELProcess(), analysisResult);
		}
	}

	public List<Assign> getAssignBlockList() {
		return analyzer.getAssignActivities();
	}

	public void addNewCopy() {
		Copy temp = analyzer.createCopy();
		model.getCurrentlyProcessingAssign().getCopy().add(temp);
	}

	public void deleteCopy(int selectionIndex) {
		model.getCurrentlyProcessingAssign().getCopy().remove(selectionIndex);
	}

	public void setCopyFromType(int selectionIndex) {
		setCopyType(model.getCurrentlyProcessingCopy().getFrom(), selectionIndex);
	}

	public void setCopyToType(int selectionIndex) {
		setCopyType(model.getCurrentlyProcessingCopy().getTo(), selectionIndex);
	}

	public Map<String, List<String>> resolveMessageType(Message complexType) {
		return ((Analyzer) analyzer).resolveMessageType(complexType);
	}

	public void generateMarkers(int index) {
		List<String> result = new ArrayList<>();
		Assign selected = model.getAssignList().get(index);
		for (Copy it : selected.getCopy()) {
			if (analysisResult.get(selected).contains(it)) {
				result.add("+");
			} else {
				result.add(" ");
			}
		}
		model.setMarkers(result);
	}

	public void saveProcess() {
		if (analyzer instanceof Analyzer) {
			((Analyzer) analyzer).saveProcess();
		}
	}
}
