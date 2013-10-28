package reader;

import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELResource;
import org.eclipse.emf.ecore.resource.Resource;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

/**
 * Reader class using to load BPEL process file to object model based on
 * org.eclipse.bpel.model package classes.
 * 
 * @author Marcin Maciorowski
 * 
 */
public class BPELReader extends org.eclipse.bpel.model.resource.BPELReader implements ErrorHandler {

	//private Process process = null;
	
	private BPELResource resource = null;
	

	public BPELReader() {
	}

	public BPELReader(String BPELFileLocation) {
		this();
	}

	public void loadProcess() {
		Document doc = null;
		
		read(resource, doc);
	}

	//public Process getBPELProcess() {
	//	return process;
	//}
}
