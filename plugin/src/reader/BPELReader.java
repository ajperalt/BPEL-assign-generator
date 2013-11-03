package reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.ActivitySet;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELExtensibleElement;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;

/**
 * Reader class using to load BPEL process file to object model based on
 * org.eclipse.bpel.model package classes.
 * 
 * @author Marcin Maciorowski
 * 
 */
public class BPELReader {

	private Process process = null;
	private String BPELFileLocation = null;
	private BPELResource resource = null;
	private org.eclipse.bpel.model.resource.BPELReader reader = null;

	// temporary check list of all activities in the process
	private ActivitySet activities = null;

	public BPELReader() {
		this.activities = new ActivitySet();
	}

	public BPELReader(String newBPELFileLocation) {
		this();
		this.BPELFileLocation = newBPELFileLocation;
		this.reader = new org.eclipse.bpel.model.resource.BPELReader();
	}

	public void loadProcess() {
		URI uri = URI.createFileURI(this.BPELFileLocation);
		Factory factory = Resource.Factory.Registry.INSTANCE.getFactory(uri);
		this.resource = (BPELResource) factory.createResource(uri);
		try {
			this.resource.load(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.process = (Process) resource.getContents().get(0);

		/** temporary */
		this.activities.createProcessActivity(this.process);
	}

	public String saveProcess() {
		// try {
		// this.resource.save(this.process.getExtensionAttributes());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		/** Only for tests */
		URI uri = URI.createFileURI("E:/private/Dropbox/engineer/project/aag_test/tests/test.bpel");
		Factory factory = Resource.Factory.Registry.INSTANCE.getFactory(uri);
		BPELResource retRes = (BPELResource) factory.createResource(uri);
		retRes.getContents().add(this.process);

		Map<String, String> map = new HashMap<String, String>();
		map.put("bpel", "http://docs.oasis-open.org/wsbpel/2.0/process/executable");
		map.put("tns", "http://matrix.bpelprocess");
		map.put("xsd", "http://www.w3.org/2001/XMLSchema");
		try {
			retRes.save(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.process.getName();
	}

	/** Accessors section */
	public Process getBPELProcess() {
		return process;
	}

	public void setBPELFileLocation(String newBPELFileLocation) {
		this.BPELFileLocation = newBPELFileLocation;
	}

	public org.eclipse.bpel.model.resource.BPELReader getReader() {
		return reader;
	}
}
