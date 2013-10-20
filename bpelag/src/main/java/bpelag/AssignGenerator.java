package bpelag;

import org.eclipse.bpel.ui.util.BPELReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class AssignGenerator implements IApplication {

	public static void main(String[] args) {
		BPELReader bpelReader = new BPELReader();

		IPath pathFile = new Path(
				"E:/private/Dropbox/engineer/project/aag_test/IBMExample/processes/travelbooking.bpel");

		Resource processResource = new ResourceImpl();
		IFile modelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(pathFile);
		ResourceSet resourceSet = new ResourceSetImpl();

		bpelReader.read(processResource, modelFile, resourceSet);

		// only for testing
		// init();
		// BPELPlugin bpelPlugin = new BPELPlugin();
		// BPELReader reader = new BPELReader();
		//
		// Resource processResource = new ResourceImpl();

		// reader.read(processResource, modelFile, resourceSet);
		//
		// Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
		// new BPELResourceFactoryImpl());
		//
		// // Process process =
		// //
		// loadProcess("E:\\private\\Dropbox\\engineer\\project\\aag_test\\IBMexamples\\processes\\travelbookingBPEL.bpel");
		// Process process =
		// loadProcess("E:/private/Dropbox/engineer/project/aag_test/IBMexamples/processes/travelbookingBPEL.bpel");

		System.out.println("Yo!");

	}

	// private static void init() {
	// BPELPlugin bpelPlugin = new BPELPlugin();
	//
	// Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
	// new BPELResourceFactoryImpl());
	// }
	//
	// private static org.eclipse.bpel.model.Process loadProcess(String
	// fileName) {
	// ResourceSet resourceSet = new ResourceSetImpl();
	// URI uri = URI.createFileURI(fileName);
	// // Resource resource =
	// // resourceSet.createResource(URI.createFileURI("C:\\"), "dupa");
	// Resource resource = resourceSet.getResource(uri, true);
	// return null; // (org.eclipse.bpel.model.Process)
	// // resource.getContents().get(0);
	// }
	//
	// protected void loadModel() {
	// // TODO: This two lines are a workaround for
	// // https://bugs.eclipse.org/bugs/show_bug.cgi?id=72565
	// // EcorePackage instance = EcorePackage.eINSTANCE;
	// // instance.eAdapters();
	// //
	// // Resource bpelResource =
	// // editModelClient.getPrimaryResourceInfo().getResource();
	// //
	// // IFile file = getFileInput();
	// // BPELReader reader = new BPELReader();
	// // reader.read(bpelResource, file, getResourceSet());
	// //
	// // this.process = reader.getProcess();
	// // if (getEditDomain() != null) {
	// // ((BPELEditDomain) getEditDomain()).setProcess(process);
	// // }
	// // this.extensionsResource = reader.getExtensionsResource();
	// // this.extensionMap = reader.getExtensionMap();
	// //
	// // this.modelListenerAdapter.setExtensionMap(extensionMap);
	// //
	// // ProcessExtension processExtension = (ProcessExtension)
	// // ModelHelper.getExtension(process);
	// // long stamp = processExtension.getModificationStamp();
	// // // Be nice if the file is old or doesn't yet have a stamp.
	// // if (stamp != 0) {
	// // long actualStamp = file.getLocalTimeStamp();
	// // if (stamp != actualStamp) {
	// // // Inform the user that visual information will be discarded,
	// // // and null out the extension map.
	// // // Only inform the user if we actually have a shell; headless
	// // // clients
	// // // will not be informed.
	// // if (getSite() != null) {
	// // MessageDialog
	// // .openWarning(
	// // getSite().getShell(),
	//		//									Messages.getString("BPELEditor.Process_Out_Of_Sync_2"), Messages.getString("BPELEditor.Process_has_been_modified_3")); //$NON-NLS-1$ //$NON-NLS-2$
	// // }
	// // // TODO: Null out and recreate the extension map. Perhaps we
	// // // need
	// // // to preserve some interesting bits of info about spec
	// // // compliance
	// // // and implicit sequences. Don't null it out yet until we
	// // // understand
	// // // all the cases in which this could occur.
	// // // extensionMap = null;
	// // }
	// // }
	// }

	public Object start(IApplicationContext arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
