package pl.eiti.bpelcg.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.ui.part.FileEditorInput;

import pl.eiti.bpelcg.ui.AnalyzerWizard;
import pl.eiti.bpelcg.util.Messages;
import pl.eiti.bpelcg.util.Settings;

/**
 * Action implements workbench action delegate. The action proxy will be created
 * by the workbench and shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be delegated to it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class CopyGenerateAction implements IWorkbenchWindowActionDelegate {
	@SuppressWarnings("unused")
	private IWorkbenchWindow window = null;

	/**
	 * The constructor.
	 */
	public CopyGenerateAction() {
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
			editor.doSave(null);
		}

		if (null != editor) {
			input = editor.getEditorInput();
		}

		if (input instanceof FileEditorInput) {
			path = ((FileEditorInput) input).getPath();

			// IPath bakPath = new Path(path.lastSegment() + ".bak"); //new
			// Path(path.toString() + ".bak");
			//
			// try {
			// ((FileEditorInput) input).getFile().copy(bakPath, Boolean.TRUE,
			// null);
			// } catch (CoreException e) {
			// e.printStackTrace();
			// }
		}

		if (path != null && Settings.BPEL_EXTENSION.equalsIgnoreCase(path.getFileExtension())) {

			WizardDialog dialog = new WizardDialog(currentShell, new AnalyzerWizard(path.toString()));
			dialog.open();
			if (input instanceof FileEditorInput) {
				try {
					((FileEditorInput) input).getFile().refreshLocal(IResource.DEPTH_ZERO, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
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
		myConsole.newMessageStream();
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
}