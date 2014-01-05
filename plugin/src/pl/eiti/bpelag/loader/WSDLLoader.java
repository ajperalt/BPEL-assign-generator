package pl.eiti.bpelag.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.internal.impl.PartImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceFactoryRegistry;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDElementDeclaration;

import pl.eiti.bpelag.util.StringElemUtil;

public class WSDLLoader {
	private List<Message> wsdlMessages = null;
	private WSDLResourceFactoryRegistry factory = null;

	/**
	 * Default constructor.
	 */
	public WSDLLoader() {
		wsdlMessages = new ArrayList<>();
	}

	/**
	 * WSDL load method.
	 * 
	 * @param importLocations
	 *            WSDL files path list
	 * @param absolutePath
	 *            absolute path to BPEL process file
	 */
	@SuppressWarnings({ "unchecked", "restriction" })
	public void load(List<String> importLocations, String absolutePath) {
		for (String location : importLocations) {
			String absoluteLocation = "";
			if (StringElemUtil.isRelative(location)) {
				absoluteLocation += absolutePath;
			}
			absoluteLocation += location;

			// TODO
			WSDLResourceImpl wsdlElem = loadWSDL(absoluteLocation);

			wsdlMessages.addAll(wsdlElem.getDefinition().getEMessages());

			for (Object it : wsdlElem.getDefinition().getEMessages()) {
				if (it instanceof Message) {
					Map<String, PartImpl> temp = ((Message) it).getParts();
					for (String iter : temp.keySet()) {
						PartImpl blabla = (PartImpl) ((Message) it).getParts().get(iter);
						// TreeIterator<EObject> iterator =
						// blabla.getElementDeclaration().getTypeDefinition().eAllContents();
						if (null != blabla) {
							TreeIterator<EObject> iterator = blabla.getElementDeclaration().eAllContents();
							while (iterator.hasNext()) {
								EObject object = (EObject) iterator.next();

								if (object instanceof XSDElementDeclaration) {
									XSDElementDeclaration xsdED = (XSDElementDeclaration) object;
									System.out.println(((Message)it).getQName().getLocalPart() + "." + iter + "." + xsdED.getName());
								}
							}
						}
					}
					System.out.println("==========");
				}
			}

			// imports.put(location, loadWSDL(nonRelLoc));
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

	public Message getMessage(String mesName) {
		Message returnElement = null;

		for (Message it : wsdlMessages) {
			if (mesName.equals(it.getQName().getLocalPart())) {
				returnElement = it;
				break;
			}
		}

		return returnElement;
	}
}
