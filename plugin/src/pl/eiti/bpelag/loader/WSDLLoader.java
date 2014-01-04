package pl.eiti.bpelag.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.util.WSDLResourceFactoryRegistry;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

import pl.eiti.bpelag.util.StringElemUtil;

public class WSDLLoader {
	private Map<String, WSDLElement> imports = null;
	private WSDLResourceFactoryRegistry factory = null;

	/**
	 * Default constructor.
	 */
	public WSDLLoader() {
		imports = new HashMap<String, WSDLElement>();
	}

	/**
	 * WSDL load method.
	 * 
	 * @param importLocations
	 *            WSDL files path list
	 * @param absolutePath
	 *            absolute path to BPEL process file
	 */
	public void load(List<String> importLocations, String absolutePath) {
		for (String location : importLocations) {
			String nonRelLoc = "";
			if (StringElemUtil.isRelative(location)) {
				nonRelLoc += absolutePath;
			}
			nonRelLoc += location;
			imports.put(location, loadWSDL(nonRelLoc));
		}
	}

	/**
	 * Single WSDL file load method.
	 * 
	 * @param location
	 *            absolute WSDL file path
	 * @return WSDL loaded
	 */
	private WSDLElement loadWSDL(String location) {
		URI uri = URI.createFileURI(location);
		ResourceSetImpl resourceSet = new ResourceSetImpl();
		Map<Object, Object> options = resourceSet.getLoadOptions();
		options.put(WSDLResourceImpl.TRACK_LOCATION, Boolean.TRUE);
		options.put(WSDLResourceImpl.CONTINUE_ON_LOAD_ERROR, Boolean.TRUE);

		factory = new WSDLResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
		resourceSet.setResourceFactoryRegistry(factory);

		WSDLResourceImpl resourceImpl = (WSDLResourceImpl) resourceSet.getResource(uri, true);
		try {
			resourceImpl.load(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (WSDLElement) resourceImpl.getContents().get(0);
	}

	/** Accessor's section */
	public Map<String, WSDLElement> getImports() {
		return imports;
	}

	public WSDLElement getImport(String importLocation) {
		return imports.get(importLocation);
	}
}
