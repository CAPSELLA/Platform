package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;


import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gr.uoa.di.madgik.datatransformation.harvester.core.harvestedmanagement.utils.XmlNodeTransform;

public class ParseDomForIdentifier {

	public static String extractIdentifier(Node node) throws XPathExpressionException {
		Node header = ((Element) node).getElementsByTagName("header").item(0);
		
		String ident = ((Element)header).getElementsByTagName("identifier").item(0).getTextContent();
		return ident;
	}

	public static String extractMetadata(Node node) throws XPathExpressionException {
		Node metadata = ((Element) node).getElementsByTagName("metadata").item(0);

		return XmlNodeTransform.nodeToString(metadata);
	}
	
	public static Set<String> extractDeleted(Node node) throws XPathExpressionException {
		Node header = ((Element) node).getElementsByTagName("header[@status='deleted']").item(0);
		Set<String> ids = new HashSet<String>();
		
		NodeList nl = ((Element)header).getElementsByTagName("identifier");
		
		int length = nl.getLength();
		
		for (int i=0; i<length; i++) {
			String id = nl.item(i).getTextContent();
			ids.add(id);
		}
		return ids;
	}
	
	public static String extractIdentifierNotToDelete(Node node) throws XPathExpressionException {
		Node header = ((Element) node).getElementsByTagName("header").item(0);
	
		String id = ((Element)header).getElementsByTagName("/ListRecords/record/header[not(@status='deleted')]/identifier/text()").item(0).getTextContent();
		return id;
	}
	
}