package pl.eiti.bpelag.ui;

import org.eclipse.jface.wizard.Wizard;

import pl.eiti.bpelag.util.AssignGenConst;

public class AnalyzerWizard extends Wizard {
	protected InfoPage infoPage;
	protected AssignPage assignPage;

	public AnalyzerWizard() {
		super();
		setNeedsProgressMonitor(Boolean.TRUE);
	}

	@Override
	public void addPages() {
		infoPage = new InfoPage(AssignGenConst.WIZARD_INFO_PAGE_NAME);
		assignPage = new AssignPage(AssignGenConst.WIZARD_ASSIGN_PAGE_NAME);
		addPage(infoPage);
		addPage(assignPage);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
