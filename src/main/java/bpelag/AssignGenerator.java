package bpelag;

import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.resource.BPELResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class AssignGenerator implements IApplication {

	public static void main(String[] args) {
		// only for testing
		
		AssignGenerator assignGen = new AssignGenerator();
		
		System.out.println("Yo!");

	}
	
	private static void init() {
		BPELPlugin bpelPlugin = new BPELPlugin();
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new BPELResourceFactoryImpl());
	}

	public Object start(IApplicationContext arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
