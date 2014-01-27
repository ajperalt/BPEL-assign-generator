package pl.eiti.bpelcg.dao;

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

import pl.eiti.bpelcg.util.StringElemUtil;

/**
 * WSDL DAO (Data Access Object) closes the functionality of loading WSDL files
 * for retrieving messages elements.
 */
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

	/**
	 * Gets singleton instance of WSDLDAO object reference.
	 * 
	 * @return reference to WSDL DAO object.
	 */
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

	/**
	 * Gets list of all messages loaded.
	 * 
	 * @return list of messages loaded by DAO object.
	 */
	public List<Message> getMessages() {
		return wsdlMessages;
	}

	/**
	 * Gets message with given qName element.
	 * 
	 * @param qName
	 *            query name.
	 * @return WSDL message element.
	 */
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
