package pl.eiti.bpelag.ui;

import org.eclipse.bpel.model.Variable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

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

	private List copyElemList = null;

	private Button newButton = null;
	private Button delButton = null;
	private Button moveUpButton = null;
	private Button moveDownButton = null;

	private Label copyFromLabel = null;
	private Combo copyFromCombo = null;
	private Tree copyFromList = null;

	private Label copyToLabel = null;
	private Combo copyToCombo = null;
	private Tree copyToList = null;

	protected AssignPage(String pageName, AnalyzerWizardController newController, AnalyzerWizardModel newModel) {
		super(pageName);
		setTitle(Messages.WIZARD_ASSIGN_PAGE_TITLE);
		model = newModel;
		controller = newController;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		mainContainer = new Composite(parent, SWT.NULL);

		setControl(mainContainer);
		mainContainer.setLayout(new RowLayout(SWT.HORIZONTAL));

		assignContainer = new Composite(mainContainer, SWT.NONE);
		RowLayout rl_assignContainer = new RowLayout(SWT.VERTICAL);
		assignContainer.setLayout(rl_assignContainer);
		assignContainer.setLayoutData(new RowData(163, 227));

		Label lblAssignBlocks = new Label(assignContainer, SWT.NONE);
		lblAssignBlocks.setText(Messages.ASSIGN_LABEL_ASSIGNBLOCKS);

		assignList = new List(assignContainer, SWT.BORDER);
		assignList.setLayoutData(new RowData(150, 200));

		copyContainer = new Composite(mainContainer, SWT.NONE);
		RowLayout rl_copyContainer = new RowLayout(SWT.VERTICAL);
		copyContainer.setLayout(rl_copyContainer);
		copyContainer.setLayoutData(new RowData(219, 273));

		Label lblCopyElements = new Label(copyContainer, SWT.NONE);
		lblCopyElements.setText(Messages.ASSIGN_LABEL_COPYELEMENTS);

		copyElemList = new List(copyContainer, SWT.BORDER);
		copyElemList.setLayoutData(new RowData(200, 145));

		copyButtonContainer = new Composite(copyContainer, SWT.NONE);
		copyButtonContainer.setLayout(new RowLayout(SWT.HORIZONTAL));
		copyButtonContainer.setLayoutData(new RowData(200, 66));

		newButton = new Button(copyButtonContainer, SWT.NONE);
		newButton.setLayoutData(new RowData(95, SWT.DEFAULT));
		newButton.setText(Messages.ASSIGN_BUTTON_NEW);

		delButton = new Button(copyButtonContainer, SWT.NONE);
		delButton.setLayoutData(new RowData(95, SWT.DEFAULT));
		delButton.setText(Messages.ASSIGN_BUTTON_DELETE);

		moveUpButton = new Button(copyButtonContainer, SWT.NONE);
		moveUpButton.setGrayed(true);
		moveUpButton.setLayoutData(new RowData(95, SWT.DEFAULT));
		moveUpButton.setText(Messages.ASSIGN_BUTTON_MOVEUP);

		moveDownButton = new Button(copyButtonContainer, SWT.NONE);
		moveDownButton.setLayoutData(new RowData(95, SWT.DEFAULT));
		moveDownButton.setText(Messages.ASSIGN_BUTTON_MOVEDOWN);

		copyFromContainer = new Composite(mainContainer, SWT.NONE);
		copyFromContainer.setLayout(new RowLayout(SWT.VERTICAL));
		copyFromContainer.setLayoutData(new RowData(210, 285));

		copyFromComboContainer = new Composite(copyFromContainer, SWT.NONE);
		RowLayout rl_copyFromComboContainer = new RowLayout(SWT.HORIZONTAL);
		rl_copyFromComboContainer.justify = true;
		rl_copyFromComboContainer.center = true;
		copyFromComboContainer.setLayout(rl_copyFromComboContainer);
		copyFromComboContainer.setLayoutData(new RowData(201, 30));

		copyFromLabel = new Label(copyFromComboContainer, SWT.NONE);
		copyFromLabel.setText(Messages.ASSIGN_LABEL_FROM);

		copyFromCombo = new Combo(copyFromComboContainer, SWT.READ_ONLY);
		copyFromCombo.setLayoutData(new RowData(100, SWT.DEFAULT));

		copyFromList = new Tree(copyFromContainer, SWT.BORDER);
		copyFromList.setLayoutData(new RowData(180, 172));

		copyToContainer = new Composite(mainContainer, SWT.NONE);
		copyToContainer.setLayout(new RowLayout(SWT.VERTICAL));
		copyToContainer.setLayoutData(new RowData(210, 285));

		copyToComboContainer = new Composite(copyToContainer, SWT.NONE);
		RowLayout rl_copyToComboContainer = new RowLayout(SWT.HORIZONTAL);
		rl_copyToComboContainer.center = true;
		rl_copyToComboContainer.justify = true;
		copyToComboContainer.setLayout(rl_copyToComboContainer);
		copyToComboContainer.setLayoutData(new RowData(195, 30));

		copyToLabel = new Label(copyToComboContainer, SWT.NONE);
		copyToLabel.setText(Messages.ASSIGN_LABEL_TO);

		copyToCombo = new Combo(copyToComboContainer, SWT.READ_ONLY);
		copyToCombo.setLayoutData(new RowData(100, SWT.DEFAULT));

		copyToList = new Tree(copyToContainer, SWT.BORDER);
		copyToList.setLayoutData(new RowData(180, 172));

		/** Data load section */

		for (String assignName : model.getAssignNameList()) {
			assignList.add(assignName);
		}

		addDataToCombo(copyFromCombo, model.getFromComboList());
		addDataToCombo(copyToCombo, model.getToComboList());

		/** Add listeners section */
		assignList.addSelectionListener(new AssignSelectionListener());
		copyElemList.addSelectionListener(new CopySelectionListener());
		newButton.addSelectionListener(new NewCopySelectionListener());
		delButton.addSelectionListener(new DeleteCopySelectionListener());
		copyFromCombo.addSelectionListener(new FromTypeSelectionListener());
		copyToCombo.addSelectionListener(new ToTypeSelectionListener());
		copyFromList.addSelectionListener(new FromElementSelectionListener());
		copyToList.addSelectionListener(new ToElementSelectionListener());
	}

	private void addDataToCombo(Combo combobox, java.util.List<String> list) {
		for (String it : list) {
			combobox.add(it);
		}
	}

	/**
	 * Instance of selection listener class that listening for select on Assign
	 * blocks list element.
	 */
	private class AssignSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			int selectionIndex = assignList.getSelectionIndex();

			copyElemList.removeAll();
			copyFromCombo.deselectAll();
			copyToCombo.deselectAll();
			copyFromList.removeAll();
			copyToList.removeAll();

			for (String elem : model.getCopyListNames(selectionIndex)) {
				copyElemList.add(elem);
			}
		}
	}

	/**
	 * Instance of selection listener that listening for select on Copy elements
	 * list.
	 */
	private class CopySelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO nobody knows what to do here.
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			Integer fromIndex = model.getFromTypeCopyIndex(copyElemList.getSelectionIndex());
			Integer toIndex = model.getToTypeCopyIndex(copyElemList.getSelectionIndex());

			if (null != fromIndex) {
				copyFromCombo.select(fromIndex);
			}
			if (null != toIndex) {
				copyToCombo.select(toIndex);
			}

			if (copyFromList.getItems().length <= 0 && copyToList.getItems().length <= 0) {
				for (Variable var : model.getProcessVariables()) {
					String itemLabel = var.getName();

					if (null != var.getType()) {
						itemLabel += " : " + var.getType().getName();
					}

					TreeItem fromItem = new TreeItem(copyFromList, SWT.NONE);
					fromItem.setText(itemLabel);

					TreeItem toItem = new TreeItem(copyToList, SWT.NONE);
					toItem.setText(itemLabel);
				}
			}
		}
	}

	/**
	 * Instance of selection listener that listening for select on copy from
	 * type combo.
	 */
	private class FromTypeSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO nobody knows what to do here.
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO when from combo selected hide/show specific component to fill from element.
		}

	}

	/**
	 * Instance of selection listener that listening for select on copy to type
	 * combo.
	 */
	private class ToTypeSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO nobody knows what to do here.
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO when to combo selected hide/show specific component to fill to element.
		}

	}

	/**
	 * Instance of selection listener that listening for select on copy from
	 * element.
	 */
	private class FromElementSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO nobody knows what to do here.
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO some magic piece of code to fill copy from selected variable.
		}

	}

	/**
	 * Instance of selection listener that listening for select on copy to
	 * element.
	 */
	private class ToElementSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO nobody knows what to do here.
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO some magic piece of code to fill copy to selected variable.
		}

	}

	private class NewCopySelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			controller.addNewCopy();
			copyElemList.removeAll();
			copyFromCombo.deselectAll();
			copyToCombo.deselectAll();
			copyFromList.removeAll();
			copyToList.removeAll();

			for (String elem : model.getCopyListNames(assignList.getSelectionIndex())) {
				copyElemList.add(elem);
			}

			copyElemList.setSelection(copyElemList.getItemCount() - 1);
		}
	}

	private class DeleteCopySelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			Integer copySelectionIndex = copyElemList.getSelectionIndex();
			controller.deleteCopy(copySelectionIndex);
			copyElemList.removeAll();
			copyFromCombo.deselectAll();
			copyToCombo.deselectAll();
			copyFromList.removeAll();
			copyToList.removeAll();

			for (String elem : model.getCopyListNames(assignList.getSelectionIndex())) {
				copyElemList.add(elem);
			}
		}

	}

}
