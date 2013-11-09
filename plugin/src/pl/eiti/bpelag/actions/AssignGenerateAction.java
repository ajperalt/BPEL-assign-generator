package pl.eiti.bpelag.actions;

import model.impl.GraphModel;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Source;
import org.eclipse.bpel.model.Target;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import reader.BPELReader;

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

	private GraphModel processModel = null;

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
		this.consoleStream.println(">>>>>>>>>> BPEL Assign Generator START <<<<<<<<<<");
		this.consoleStream.println();

		BPELReader processReader = new BPELReader(
				"E:/private/Dropbox/engineer/project/aag_test/IBMexamples/processes/travelbookingBPEL.bpel");

		processReader.loadProcess();

		this.consoleStream.print("BPEL process name: ");
		this.consoleStream.println(processReader.getBPELProcess().getName());
		this.consoleStream.println();

		this.processModel = new GraphModel(processReader.getBPELProcess());

		this.consoleStream.println(">>>>>>>>>> BPEL Process elements:");

		TreeIterator<EObject> test = processReader.getBPELProcess().eAllContents();
		 processStructurePrint(test, "");

		this.consoleStream.println();

		this.consoleStream.println("BPEL process >> " + processReader.saveProcess() + " << Saved");

		this.consoleStream.println();
		this.consoleStream.println(">>>>>>>>>> BPEL Assign Generator STOP  <<<<<<<<<<");
	}

	private void processStructurePrint(TreeIterator<EObject> input, String tab) {

		while (input.hasNext()) {
			EObject temp = input.next();
			if (temp instanceof Activity) {
				this.consoleStream.println(tab + ((Activity) temp).getName());
				TreeIterator<EObject> a = temp.eAllContents();
				processStructurePrint(a, tab + "     ");
			}
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

		// TODO fix console printing
		MessageConsole myConsole = findConsole("");
		this.consoleStream = myConsole.newMessageStream();
	}

	/**
	 * Currently not working. Eclipse instance message console finder.
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