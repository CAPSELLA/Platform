package gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gr.uoa.di.madgik.datatransformation.harvester.utils.RequestData;

public class RetrieveResumptionToken {
	private final static Logger logger = Logger.getLogger(RequestData.class);
	
	private final static String xPathExpression = "resumptionToken";
	
	public static String retrieveResumptionToken(File xmlFile, String verb) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String resumptionToken = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			XPath xPath = XPathFactory.newInstance().newXPath();
			resumptionToken = (String) xPath.compile(getExpressionForResumptionToken(verb))
												.evaluate(document, XPathConstants.STRING);
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
			return null;
		} catch (ParserConfigurationException e) {
			logger.info(e.getMessage());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return resumptionToken;
	}	
	
	public static String retrieveResumptionToken(String xmlDocument, String verb) {
		String resumptionToken = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = null;
	        try {
	        	doc = builder.parse(new InputSource(new StringReader(xmlDocument)));
	        } catch (SAXException | IOException e) {
				e.printStackTrace();
			}
			XPath xPath = XPathFactory.newInstance().newXPath();
			resumptionToken = (String) xPath.compile(getExpressionForResumptionToken(verb))
												.evaluate(doc, XPathConstants.STRING);
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
			return null;
		} catch (ParserConfigurationException e) {
			logger.info(e.getMessage());
		} catch (XPathExpressionException e) {
			logger.info(e.getMessage());
		}
		return resumptionToken;
	}	
	
	private static String getExpressionForResumptionToken(String verb) {
		return  "/OAI-PMH/" + verb + "/"+xPathExpression+"/text()";
	}
	
}
