package pl.eiti.bpelcg.ui.controller;

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
import org.eclipse.bpel.model.impl.ToImpl;
import org.eclipse.wst.wsdl.Message;

import pl.eiti.bpelcg.analyzer.IAnalysisResult;
import pl.eiti.bpelcg.analyzer.IAnalyzer;
import pl.eiti.bpelcg.analyzer.impl.Analyzer;
import pl.eiti.bpelcg.resolver.WSDLResolver;
import pl.eiti.bpelcg.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelcg.updater.IUpdater;
import pl.eiti.bpelcg.updater.impl.Updater;
import pl.eiti.bpelcg.util.Settings;

/**
 * Controller of plugin wizard.
 */
public class AnalyzerWizardController {
	private IAnalyzer analyzer = null;
	private IUpdater updater = null;

	private String pathToBPEL = null;
	private IAnalysisResult analysisResult = null;

	private AnalyzerWizardModel model = null;

	/**
	 * Default wizard controller constructor.
	 */
	public AnalyzerWizardController() {
		analyzer = new Analyzer();
		updater = new Updater();
	}

	/**
	 * Custom wizard controller constructor.
	 * 
	 * @param pathToFile
	 *            path to BPEL file.
	 * @param newModel
	 *            wizard model reference.
	 */
	public AnalyzerWizardController(final String pathToFile, AnalyzerWizardModel newModel) {
		this();
		pathToBPEL = pathToFile;
		model = newModel;
		analyzer.init(pathToBPEL);
		executeAnalyze();
		executeUpdate();
	}

	/**
	 * Executes BPEL process analyze.
	 */
	public void executeAnalyze() {
		analysisResult = analyzer.analyze();
		model.setAssignList(analyzer.getAssignActivities());
		model.setProcessVariables(analyzer.getProcessVariables());
	}

	/**
	 * Executes BPEL process update.
	 */
	public void executeUpdate() {
		if (analyzer instanceof Analyzer) {
			updater.update(((Analyzer) analyzer).getBPELProcess(), analysisResult);
		}
	}

	/**
	 * Gets all assign instruction from the process.
	 * 
	 * @return list of assigns.
	 */
	public List<Assign> getAssignBlockList() {
		return analyzer.getAssignActivities();
	}

	/**
	 * Adds new copy element.
	 */
	public void addNewCopy() {
		Copy temp = analyzer.createCopy();
		model.getCurrentlyProcessingAssign().getCopy().add(temp);
	}

	/**
	 * Deletes selected copy element.
	 * 
	 * @param selectionIndex
	 *            index of element to delete.
	 */
	public void deleteCopy(int selectionIndex) {
		model.getCurrentlyProcessingAssign().getCopy().remove(selectionIndex);
	}

	/**
	 * Delegates resolve of the complex type message.
	 * 
	 * @param complexType
	 *            complex type message.
	 * @return resolved message information (structured form using map).
	 */
	public Map<String, List<String>> resolveMessageType(Message complexType) {
		return WSDLResolver.getInstance().resolveMessageType(complexType);
	}

	/**
	 * Generates new copy elements marker list.
	 * 
	 * @param index
	 *            assign element index.
	 */
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

	/**
	 * Delegates BPEL process save command.
	 */
	public void saveProcess() {
		if (analyzer instanceof Analyzer) {
			((Analyzer) analyzer).saveProcess();
		}
	}

	/**
	 * Creates from element.
	 * 
	 * @param elements
	 *            concatenated message information.
	 * @return BPEL from element.
	 */
	public From createFromVarPart(java.util.List<String> elements) {
		From newFrom = updater.createNewFrom();

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

			Query newQuery = updater.createNewQuery();
			newQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
			newQuery.setValue(newFrom.getVariable().getMessageType().getQName().getPrefix() + ":"
					+ varSplitted[0].trim());
			newFrom.setQuery(newQuery);
		}

		return newFrom;
	}

	/**
	 * Creates to element.
	 * 
	 * @param elements
	 *            concatenated message information.
	 * @return BPEL to element.
	 */
	public To createToVarPart(java.util.List<String> elements) {
		To newTo = updater.createNewTo();

		int size = elements.size();
		int index = size - 1;

		if (0 < elements.size()) {
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
					newTo.setVariable(it);
					break;
				}
			}
		}

		if (1 < elements.size()) {
			String[] varSplitted = elements.get(index).split(":");
			index--;
			if (newTo instanceof ToImpl) {
				((ToImpl) newTo).setPartName(varSplitted[0].trim());
			}
		}

		if (2 < elements.size()) {
			String[] varSplitted = elements.get(index).split(":");
			index--;

			Query newQuery = updater.createNewQuery();
			newQuery.setQueryLanguage(Settings.QUERY_LANGUAGE);
			newQuery.setValue(newTo.getVariable().getMessageType().getQName().getPrefix() + ":" + varSplitted[0].trim());
			newTo.setQuery(newQuery);
		}

		return newTo;
	}

	/**
	 * Moves up copy elements.
	 * 
	 * @param copySelectionIndex
	 *            copy instruction to move up index.
	 */
	public void moveUpCopy(int copySelectionIndex) {
		Copy temp = model.getCurrentlyProcessingAssign().getCopy().get(copySelectionIndex);
		model.getCurrentlyProcessingAssign().getCopy().remove(temp);
		model.getCurrentlyProcessingAssign().getCopy().add(copySelectionIndex - 1, temp);
	}

	/**
	 * Moves down copy elements.
	 * 
	 * @param copySelectionIndex
	 *            copy instruction to move down index.
	 */
	public void moveDownCopy(int copySelectionIndex) {
		Copy temp = model.getCurrentlyProcessingAssign().getCopy().get(copySelectionIndex);
		model.getCurrentlyProcessingAssign().getCopy().remove(temp);
		model.getCurrentlyProcessingAssign().getCopy().add(copySelectionIndex + 1, temp);
	}
}
