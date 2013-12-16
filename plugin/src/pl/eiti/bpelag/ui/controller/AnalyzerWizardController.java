package pl.eiti.bpelag.ui.controller;

import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.To;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.analyzer.impl.Analyzer;
import pl.eiti.bpelag.generator.IGenerator;
import pl.eiti.bpelag.generator.impl.Generator;
import pl.eiti.bpelag.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelag.util.Messages;

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
		model.setAssignList(analyzer.getAssignActivities());
		model.setProcessVariables(analyzer.getProcessVariables());
	}

	public void executeAnalyze() {
		analysisResult = analyzer.analyze();
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

	private void setCopyType(EObject copyTypeElem, int selectionIndex) {
		String copyType = null;
		From fromType = null;
		To toType = null;
		
		if (copyTypeElem instanceof From) {
			copyType = model.getFromComboList().get(selectionIndex);
			fromType = (From) copyTypeElem;
		} else if(copyTypeElem instanceof To) {
			copyType = model.getToComboList().get(selectionIndex);
			toType = (To) copyTypeElem;
		}

		switch(copyType) {
		case Messages.ASSIGN_CATEGORY_VARPART:
			// TO
			break;
		case Messages.ASSIGN_CATEGORY_EXPRESSION:
			// TO
			break;
		case Messages.ASSIGN_CATEGORY_LITERAL:
			
			break;
		case Messages.ASSIGN_CATEGORY_VARPROPERTY:
			// TO
			break;
		case Messages.ASSIGN_CATEGORY_PARTNERROLE:
			// TO
			break;
		case Messages.ASSIGN_CATEGORY_ENDPOINTREF:
			break;
		case Messages.ASSIGN_CATEGORY_OPAQUE:
			break;
		// TODO finish him
		}
	}
}
