package pl.eiti.bpelag.actions;

import java.util.Set;

import org.eclipse.bpel.model.Activity;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.part.FileEditorInput;

import pl.eiti.bpelag.model.graph.GraphNode;
import pl.eiti.bpelag.model.impl.GraphModel;
import pl.eiti.bpelag.reader.BPELReader;
import pl.eiti.bpelag.transformer.impl.GraphTransformer;
import pl.eiti.bpelag.ui.AnalyzerWizard;
import pl.eiti.bpelag.util.ActivityUtil;
import pl.eiti.bpelag.util.Messages;
import pl.eiti.bpelag.util.Settings;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class AssignGenerateAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window = null;
	private MessageConsoleStream consoleStream = null;

	/**
	 * The constructor.
	 */
	public AssignGenerateAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		Shell currentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = null;
		IWorkbenchPage activePage = null;
		IEditorPart editor = null;
		IEditorInput input = null;
		IPath path = null;

		if (null != workbench) {
			window = workbench.getActiveWorkbenchWindow();
		}

		if (null != window) {
			activePage = window.getActivePage();
		}

		if (null != activePage) {
			editor = activePage.getActiveEditor();
		}

		if (null != editor) {
			input = editor.getEditorInput();
		}

		if (input instanceof FileEditorInput) {
			path = ((FileEditorInput) input).getPath();
		}

		if (path != null && Settings.BPEL_EXTENSION.equalsIgnoreCase(path.getFileExtension())) {
			WizardDialog dialog = new WizardDialog(currentShell, new AnalyzerWizard(path.toString()));
			dialog.open();
		} else {
			MessageDialog.openInformation(currentShell, Messages.DIALOG_TITLE_BAR, Messages.DIALOG_NO_BPEL_FILE);
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
		MessageConsole myConsole = findConsole("");
		this.consoleStream = myConsole.newMessageStream();
	}

	/**
	 * Eclipse instance message console finder.
	 * 
	 */
	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	/** BELOW - ONLY FOR TESTING */
	/**
	 * Temporary method.
	 */
	@SuppressWarnings("unused")
	private void MainTest() {
		BPELReader processReader = new BPELReader(
				"E:/private/Dropbox/engineer/project/aag_test/IBMexamples/processes/travelbookingBPEL.bpel");

		processReader.loadProcess();
		GraphTransformer transformer = GraphTransformer.instance();

		// this.consoleStream.print("BPEL process name: ");
		// this.consoleStream.println(processReader.getBPELProcess().getName());
		// this.consoleStream.println();

		// this.processModel = new GraphModel(processReader.getBPELProcess());

		this.consoleStream.println(">>>>>>>>>> BPEL Process elements:");

		// TreeIterator<EObject> test =
		// processReader.getBPELProcess().eAllContents();
		// Set<Activity> processed = new HashSet<>();
		// processStructurePrint(test, "", processed);

		processModelPrint((GraphModel) transformer.ProcessToModel(processReader.getBPELProcess()));

		// IModel model = (GraphModel)
		// GraphTransformer.instance().ProcessToModel(processReader.getBPELProcess());
		//
		// processModelPrint(model);

		this.consoleStream.println();

		this.consoleStream.println("BPEL process >> " + processReader.saveProcess() + " << Saved");

		this.consoleStream.println();
		this.consoleStream.println(">>>>>>>>>> BPEL Assign Generator STOP  <<<<<<<<<<");
	}

	private void processModelPrint(GraphModel processToModel) {
		this.consoleStream.println();
		graphPrint(processToModel.getRoot());
	}

	/**
	 * Temporary method.
	 */
	@SuppressWarnings("unused")
	private void processStructurePrint(TreeIterator<EObject> input, String tab, Set<Activity> processedActivities) {

		while (input.hasNext()) {
			EObject temp = input.next();
			if (temp instanceof Activity && !(processedActivities.contains((Activity) temp))) {
				processedActivities.add((Activity) temp);
				if (ActivityUtil.isBasicActivity((Activity) temp)) {
					this.consoleStream.println(tab + ((Activity) temp).getName());
				} else {
					this.consoleStream.println(tab + ((Activity) temp).getName());
					TreeIterator<EObject> a = temp.eAllContents();
					processStructurePrint(a, tab + "     ", processedActivities);
					this.consoleStream.println(tab + ((Activity) temp).getName());
				}
			}
		}
	}

	private void graphPrint(GraphNode<Activity> node) {
		this.consoleStream.println(node.getData().getName());
		for (GraphNode<Activity> it : node.getNextNodes()) {
			graphPrint(it);
		}
	}
}