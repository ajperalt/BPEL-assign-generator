package pl.eiti.bpelag.ui;

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
	}

	@Override
	public void createControl(Composite parent) {
		// TODO information page
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		setPageComplete(true);
	}
}
