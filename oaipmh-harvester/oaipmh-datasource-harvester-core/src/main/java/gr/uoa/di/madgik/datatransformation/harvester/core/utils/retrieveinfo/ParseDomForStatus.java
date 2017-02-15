package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;


import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ParseDomForStatus {

	public static String extractIdentifier(Node node) throws XPathExpressionException {
		Node header = ((Element) node).getElementsByTagName("header").item(0);
		String status = ((Element)header).getAttribute("status");
		
		return status;
	}

}