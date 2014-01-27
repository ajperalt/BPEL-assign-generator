package pl.eiti.bpelcg.ui;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import pl.eiti.bpelcg.ui.controller.AnalyzerWizardController;
import pl.eiti.bpelcg.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelcg.util.Messages;

public class InfoPage extends WizardPage {
	@SuppressWarnings("unused")
	private AnalyzerWizardModel model = null;
	private AnalyzerWizardController controller = null;

	private Composite container;

	protected InfoPage(String pageName, AnalyzerWizardController newController, AnalyzerWizardModel newModel) {
		super(pageName);
		setTitle(Messages.WIZARD_INFO_PAGE_TITLE);
		model = newModel;
		controller = newController;
		setPageComplete(false);
	}

	@Override
	public IWizardPage getNextPage() {
		setPageComplete(true);

		IWizardPage nextPage = super.getNextPage();
		return nextPage;
	}

	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		setControl(container);
	}
}
