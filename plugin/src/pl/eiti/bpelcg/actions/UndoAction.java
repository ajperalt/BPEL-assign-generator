package pl.eiti.bpelcg.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import pl.eiti.bpelcg.util.Settings;

/**
 * Action implements workbench action delegate. The action proxy will be created
 * by the workbench and shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be delegated to it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class UndoAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction arg0) {
		@SuppressWarnings("unused")
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

		Path bakPath = null;
		if (input instanceof FileEditorInput) {
			path = ((FileEditorInput) input).getPath();
			if (Settings.BPEL_EXTENSION.equalsIgnoreCase(path.getFileExtension())) {
				bakPath = new Path(path.toString() + ".bak");

				try {
					Files.copy(bakPath.toFile().toPath(), path.toFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (input instanceof FileEditorInput) {
					try {
						((FileEditorInput) input).getFile().refreshLocal(IResource.DEPTH_ZERO, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
	}

}