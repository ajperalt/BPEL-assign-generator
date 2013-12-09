package pl.eiti.bpelag.ui;

import org.eclipse.bpel.model.Assign;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;

import pl.eiti.bpelag.ui.controller.AnalyzerWizardController;
import pl.eiti.bpelag.ui.model.AnalyzerWizardModel;
import pl.eiti.bpelag.util.Messages;

public class AssignPage extends WizardPage {
	private AnalyzerWizardModel model = null;
	private AnalyzerWizardController controller = null;

	private Composite mainContainer = null;
	private Composite assignContainer = null;
	private Composite copyContainer = null;
	private Composite copyFromContainer = null;
	private Composite copyToContainer = null;

	private Composite copyButtonContainer = null;
	private Composite copyFromComboContainer = null;
	private Composite copyToComboContainer = null;

	private List assignList = null;

	List copyElemList = null;

	Button newButton = null;
	Button delButton = null;

	Label copyFromLabel = null;
	Combo copyFromCombo = null;
	Tree copyFromList = null;

	Label copyToLabel = null;
	Combo copyToCombo = null;
	Tree copyToList = null;

	protected AssignPage(String pageName, AnalyzerWizardController newController, AnalyzerWizardModel newModel) {
		super(pageName);
		setTitle(Messages.WIZARD_ASSIGN_PAGE_TITLE);
		model = newModel;
		controller = newController;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO create sth similar to properties in BPEL Designer for assign
		// activities
		mainContainer = new Composite(parent, SWT.BORDER);
		mainContainer.setLayout(new RowLayout(SWT.HORIZONTAL));

		assignContainer = new Composite(mainContainer, SWT.BORDER);
		copyContainer = new Composite(mainContainer, SWT.BORDER);
		copyFromContainer = new Composite(mainContainer, SWT.BORDER);
		copyToContainer = new Composite(mainContainer, SWT.BORDER);

		RowLayout innerVertLayout = new RowLayout(SWT.VERTICAL);
		innerVertLayout.marginLeft = 0;
		innerVertLayout.marginRight = 0;
		innerVertLayout.marginTop = 0;
		innerVertLayout.marginBottom = 0;

		assignContainer.setLayout(innerVertLayout);
		copyContainer.setLayout(innerVertLayout);
		copyFromContainer.setLayout(innerVertLayout);
		copyToContainer.setLayout(innerVertLayout);

		RowLayout innerHorLayout = new RowLayout(SWT.HORIZONTAL);
		innerHorLayout.marginLeft = 0;
		innerHorLayout.marginRight = 0;
		innerHorLayout.marginTop = 0;
		innerHorLayout.marginBottom = 0;

		assignList = new List(assignContainer, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		assignList.setBounds(0, 0, 200, 200);

		for (Assign assign : controller.getAssignBlockList()) {
			assignList.add(assign.getName());
		}

		copyElemList = new List(copyContainer, SWT.BORDER | SWT.FILL);

		copyButtonContainer = new Composite(copyContainer, SWT.BORDER);
		copyButtonContainer.setLayout(innerHorLayout);

		newButton = new Button(copyButtonContainer, SWT.PUSH);
		delButton = new Button(copyButtonContainer, SWT.PUSH);

		newButton.setText(Messages.ASSIGN_BUTTON_NEW);
		delButton.setText(Messages.ASSIGN_BUTTON_DELETE);

		copyFromComboContainer = new Composite(copyFromContainer, SWT.NONE);
		copyFromComboContainer.setLayout(innerHorLayout);
		copyFromLabel = new Label(copyFromComboContainer, SWT.NONE);
		copyFromLabel.setText(Messages.ASSIGN_LABEL_FROM);
		copyFromCombo = new Combo(copyFromComboContainer, SWT.DROP_DOWN | SWT.READ_ONLY);
		copyFromList = new Tree(copyFromContainer, SWT.VIRTUAL | SWT.BORDER);

		copyToComboContainer = new Composite(copyToContainer, SWT.NONE);
		copyToComboContainer.setLayout(innerHorLayout);
		copyToLabel = new Label(copyToComboContainer, SWT.NONE);
		copyToLabel.setText(Messages.ASSIGN_LABEL_TO);
		copyToCombo = new Combo(copyToComboContainer, SWT.DROP_DOWN | SWT.READ_ONLY);
		copyToList = new Tree(copyToContainer, SWT.VIRTUAL | SWT.BORDER);

		assignList.addSelectionListener(new AssignSelectionListener());
		copyElemList.addSelectionListener(new CopySelectionListener());

		setControl(mainContainer);
	}

	private class AssignSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			copyElemList.removeAll();
			copyElemList.add(assignList.getSelection()[0]);

			// TODO handle select assign activity from the list event
			// TODO set new list to copy elements list - clear
			// TODO set new list to copy from list and copy to list
			// TODO fill list of copy elements with selected assign copy
			// activities
		}

	}

	private class CopySelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
