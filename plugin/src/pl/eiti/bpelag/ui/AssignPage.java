package pl.eiti.bpelag.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import pl.eiti.bpelag.util.AssignGenConst;

public class AssignPage extends WizardPage {

	private Composite container = null;

	private List assignList = null;

	protected AssignPage(String pageName) {
		super(pageName);
		setTitle(AssignGenConst.WIZARD_ASSIGN_PAGE_TITLE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO create sth similar to properties in BPEL Designer for assign
		// activities
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		assignList = new List(container, SWT.BORDER);
		assignList.add("Aliens");
		assignList.add("Capote");
		assignList.add("Neverending story");
		assignList.add("Starship troopers");
		assignList.add("Exorcist");
		assignList.add("Omen");

		setControl(container);
	}

}
