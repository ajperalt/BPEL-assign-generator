package pl.eiti.bpelag.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.util.WSDLResourceFactoryRegistry;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

import pl.eiti.bpelag.util.StringElemUtil;

public class WSDLDAO {
	private static WSDLDAO instance = null;

	private List<Message> wsdlMessages = null;
	private WSDLResourceFactoryRegistry factory = null;

	/**
	 * Default constructor.
	 */
	private WSDLDAO() {
		wsdlMessages = new ArrayList<>();
	}

	public static WSDLDAO getInstance() {
		if (null == instance) {
			instance = new WSDLDAO();
		}

		return instance;
	}

	/**
	 * WSDL load method.
	 * 
	 * @param importLocations
	 *            WSDL files path list
	 * @param absolutePath
	 *            absolute path to BPEL process file
	 */
	@SuppressWarnings({ "unchecked" })
	public void load(List<String> importLocations, String absolutePath) {
		for (String location : importLocations) {
			String absoluteLocation = "";
			if (StringElemUtil.isRelative(location)) {
				absoluteLocation += absolutePath;
			}
			absoluteLocation += location;

			WSDLResourceImpl wsdlElem = loadWSDL(absoluteLocation);

			wsdlMessages.addAll(wsdlElem.getDefinition().getEMessages());
		}
	}

	/**
	 * Single WSDL file load method.
	 * 
	 * @param location
	 *            absolute WSDL file path
	 * @return WSDL loaded
	 */
	private WSDLResourceImpl loadWSDL(String location) {
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

		return (WSDLResourceImpl) resourceImpl;
	}

	/** Accessor's section */
	public List<Message> getMessages() {
		return wsdlMessages;
	}

	public Message getMessage(QName qName) {
		Message returnElement = null;

		for (Message it : wsdlMessages) {
			if (qName.equals(it.getQName())) {
				returnElement = it;
				break;
			}
		}

		return returnElement;
	}
}
