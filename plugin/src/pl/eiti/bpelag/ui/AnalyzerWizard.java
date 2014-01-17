package pl.eiti.bpelag.ui;

import org.eclipse.jface.wizard.Wizard;

import pl.eiti.bpelag.ui.controller.AnalyzerWizardController;
import pl.eiti.bpelag.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelag.util.Messages;

public class AnalyzerWizard extends Wizard {
	private AnalyzerWizardController wizardController = null;
	private AnalyzerWizardModel wizardModel = null;

	protected InfoPage infoPage = null;
	protected AssignPage assignPage = null;

	public AnalyzerWizard(String pathToBPEL) {
		super();

		wizardModel = new AnalyzerWizardModel();

		wizardController = new AnalyzerWizardController(pathToBPEL, wizardModel);
		setNeedsProgressMonitor(Boolean.TRUE);
	}

	@Override
	public void addPages() {
		infoPage = new InfoPage(Messages.WIZARD_INFO_PAGE_NAME, wizardController, wizardModel);
		assignPage = new AssignPage(Messages.WIZARD_ASSIGN_PAGE_NAME, wizardController, wizardModel);
		addPage(infoPage);
		addPage(assignPage);
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	@Override
	public boolean performFinish() {
		wizardController.saveProcess();

		return true;
	}

}
