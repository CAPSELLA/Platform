package gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gr.uoa.di.madgik.datatransformation.harvester.utils.RequestData;

public class RetrieveMetadataFormats {
	private final static Logger logger = Logger.getLogger(RequestData.class);

	public static List<String> retrieveSchemas(String xmlFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		NodeList metadataPrefixes = null;
		List<String> schemas = new ArrayList<String>();
		try {
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlFile));
			Document document = builder.parse(is);
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			metadataPrefixes = (NodeList) xPath.compile(getExpressionForMetadataPrefix()).evaluate(document, XPathConstants.NODESET);
		
			for (int i=0; i<metadataPrefixes.getLength(); i++) {
				Node node = metadataPrefixes.item(i);
				schemas.add(node.getTextContent());
			}
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
			return null;
		} catch (ParserConfigurationException e) {
			logger.info(e.getMessage());
		} catch (XPathExpressionException e) {
			logger.info(e.getMessage());
		} catch (SAXException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return schemas;
	}	
	
	private static String getExpressionForMetadataPrefix() {
		return  "/OAI-PMH/ListMetadataFormats/metadataFormat/metadataPrefix/text()";
	}
}