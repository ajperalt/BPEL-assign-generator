package pl.eiti.bpelcg.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.internal.impl.PartImpl;
import org.eclipse.xsd.XSDElementDeclaration;

import pl.eiti.bpelcg.dao.WSDLDAO;
import pl.eiti.bpelcg.util.Settings;

/**
 * WSDL resolver used to retrieving information about complex and simple types
 * defined in WSDL and used to communication with service which interface
 * describes WSDL document.
 */
@SuppressWarnings("restriction")
public class WSDLResolver extends Settings {
	private static WSDLResolver instance = null;

	private WSDLResolver() {

	}

	public static WSDLResolver getInstance() {
		if (null == instance) {
			instance = new WSDLResolver();
		}

		return instance;
	}

	/**
	 * Retrieves all elements of given complex type message and create
	 * concatenated information about them delimited by M_LIMITER defined in
	 * Settings class
	 * (MessagePartName/delimiter/xsdElementTypeName/delimiter/xsdElementName).
	 * 
	 * @param complexType
	 *            defined in WSDL complex type message
	 * @return list of concatenated information about elements
	 */
	@SuppressWarnings({ "unchecked" })
	public List<String> retrieveElements(Message complexType) {
		List<String> result = new ArrayList<>();

		if (complexType instanceof Message) {
			Map<String, PartImpl> partsMap = ((Message) complexType).getParts();

			for (String partName : partsMap.keySet()) {
				PartImpl elementPart = (PartImpl) ((Message) complexType).getParts().get(partName);

				if (null != elementPart) {
					TreeIterator<EObject> iterator = elementPart.getElementDeclaration().eAllContents();

					while (iterator.hasNext()) {
						EObject object = (EObject) iterator.next();

						if (object instanceof XSDElementDeclaration) {
							XSDElementDeclaration xsdED = (XSDElementDeclaration) object;
							result.add(partName + M_LIMITER + xsdED.getType().getName() + M_LIMITER + xsdED.getName());
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Resolves message type to map part element to elements included in part
	 * element.
	 * 
	 * @param complexType
	 *            complex type message
	 * @return map of message part elements to list of elements within selected
	 *         part element
	 */
	public Map<String, List<String>> resolveMessageType(Message complexMessageType) {
		Message complexType = WSDLDAO.getInstance().getMessage(complexMessageType.getQName());

		Map<String, List<String>> result = new HashMap<String, List<String>>();

		for (String element : retrieveElements(complexType)) {
			String[] splittedElement = element.split(M_DELIMITER);
			if (result.containsKey(splittedElement[PART_NAME_INDEX])) {
				result.get(splittedElement[PART_NAME_INDEX]).add(
						splittedElement[ELEM_NAME_INDEX] + " : " + splittedElement[ELEM_TYPE_INDEX]);
			} else {
				List<String> simpleElement = new ArrayList<>();
				simpleElement.add(splittedElement[ELEM_NAME_INDEX] + " : " + splittedElement[ELEM_TYPE_INDEX]);
				result.put(splittedElement[PART_NAME_INDEX], simpleElement);
			}
		}

		return result;
	}

}
