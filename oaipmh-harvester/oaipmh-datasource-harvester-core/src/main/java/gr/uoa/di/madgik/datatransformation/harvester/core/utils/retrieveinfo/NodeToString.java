package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public class NodeToString {

	public static List<String> printXmlDocument(List<Node> nodesList) {
		List<String> nodes = new ArrayList<String>();
		
		for (Node node: nodesList) {
			StringWriter writer = new StringWriter();
			Transformer transformer;
			String xml = null;
			try {
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(new DOMSource(node), new StreamResult(writer));
				xml = writer.toString();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
			nodes.add(xml);
		}
		return nodes;
	}
	
	public static List<String> getMetadataToStringFormat(List<Node> nodesList ) {
		List<String> metadata = new ArrayList<String>();
		try {
			for (Node node: nodesList) {
				metadata.add(ParseDomForIdentifier.extractMetadata(node));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return metadata;
	}
	
	
}
