package pl.eiti.bpelag.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Query;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.impl.FromImpl;
import org.eclipse.wst.wsdl.Message;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.analyzer.IAnalyzer;
import pl.eiti.bpelag.analyzer.impl.Analyzer;
import pl.eiti.bpelag.generator.IUpdater;
import pl.eiti.bpelag.generator.impl.Updater;
import pl.eiti.bpelag.resolver.WSDLResolver;
import pl.eiti.bpelag.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelag.util.Settings;

public class AnalyzerWizardController {
	private IAnalyzer analyzer = null;
	private IUpdater generator = null;

	private String pathToBPEL = null;
	private IAnalysisResult analysisResult = null;

	private AnalyzerWizardModel model = null;

	public AnalyzerWizardController() {
		analyzer = new Analyzer();
		generator = new Updater();
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
			generator.update(((Analyzer) analyzer).getBPELProcess(), analysisResult);
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

	// public void setCopyFromType(int selectionIndex) {
	// setCopyType(model.getCurrentlyProcessingCopy().getFrom(),
	// selectionIndex);
	// }
	//
	// public void setCopyToType(int selectionIndex) {
	// setCopyType(model.getCurrentlyProcessingCopy().getTo(), selectionIndex);
	// }

	public Map<String, List<String>> resolveMessageType(Message complexType) {
		return WSDLResolver.getInstance().resolveMessageType(complexType);
		// return WSDLResolver.getInstance().resolveMessageType(complexType);
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

	public From createFromVarPart(java.util.List<String> elements) {
		From newFrom = generator.createNewFrom();

		int size = elements.size();
		int index = size - 1;

		if (0 < size) {
			String[] varSplitted = elements.get(index).split(":");
			index--;

			for (Variable it : model.getProcessVariables()) {
				Boolean typeOK = Boolean.FALSE;

				if (null != it.getMessageType()
						&& it.getMessageType().getQName().getLocalPart().equals(varSplitted[1].trim())) {
					typeOK = Boolean.TRUE;
				} else if (null != it.getType() && it.getType().getName().equals(varSplitted[1].trim())) {
					typeOK = Boolean.TRUE;
				}

				if (it.getName().equals(varSplitted[0].trim()) && typeOK) {
					newFrom.setVariable(it);
					break;
				}
			}
		}

		if (1 < size) {
			String[] varSplitted = elements.get(index).split(":");
			index--;

			if (newFrom instanceof FromImpl) {
				((FromImpl) newFrom).setPartName(varSplitted[0].trim());
			}
		}

		if (2 < size) {
			String[] varSplitted = elements.get(index).split(":");

			Query newQuery = generator.createNewQuery();
			newQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
			newQuery.setValue(newFrom.getVariable().getMessageType().getQName().getPrefix() + ":"
					+ varSplitted[0].trim());
			newFrom.setQuery(newQuery);
		}

		return newFrom;
	}

	public To createToVarPart(java.util.List<String> elements) {
		To newTo = generator.createNewTo();

		if (0 < elements.size()) {
			String[] varSplitted = elements.get(0).split(":");

			for (Variable it : model.getProcessVariables()) {
				Boolean typeOK = Boolean.FALSE;

				if (null != it.getMessageType()
						&& it.getMessageType().getQName().getLocalPart().equals(varSplitted[1].trim())) {
					typeOK = Boolean.TRUE;
				} else if (null != it.getType() && it.getType().getName().equals(varSplitted[1].trim())) {
					typeOK = Boolean.TRUE;
				}

				if (it.getName().equals(varSplitted[0].trim()) && typeOK) {
					newTo.setVariable(it);
					break;
				}
			}
		}

		if (1 < elements.size()) {
			String[] varSplitted = elements.get(1).split(":");
			if (newTo instanceof FromImpl) {
				((FromImpl) newTo).setPartName(varSplitted[0].trim());
			}
		}

		if (2 < elements.size()) {
			String[] varSplitted = elements.get(2).split(":");

			Query newQuery = generator.createNewQuery();
			newQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
			newQuery.setValue(newTo.getVariable().getMessageType().getQName().getPrefix() + ":" + varSplitted[0].trim());
		}

		return newTo;
	}
}
