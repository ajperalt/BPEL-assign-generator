package pl.eiti.bpelag.ui;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import pl.eiti.bpelag.util.AssignGenConst;

public class InfoPage extends WizardPage {
	private Composite container;

	protected InfoPage(String pageName) {
		super(pageName);
		setTitle(AssignGenConst.WIZARD_INFO_PAGE_TITLE);
		// TODO Auto-generated constructor stub
		setPageComplete(false);
	}

	@Override
	public IWizardPage getNextPage() {
		IWizardPage nextPage = super.getNextPage();

		
		// TODO magic to run analyzer
		setPageComplete(true);

		return nextPage;
	}

	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO information page
		container = new Composite(parent, SWT.NONE);
		setControl(container);
	}
}
