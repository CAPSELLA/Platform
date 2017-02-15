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
import gr.uoa.di.madgik.datatransformation.harvester.utils.container.ErrorOnData;

public class RetrieveError {
	private final static Logger logger = Logger.getLogger(RequestData.class);
	
	private final static String xPathExpressionEC = "error/@code";
	private final static String xPathExpressionEM = "error";
	
	public static ErrorOnData retrieveError(File xmlFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		String error = null;
		ErrorOnData errorResponse = new ErrorOnData();
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			XPath xPath = XPathFactory.newInstance().newXPath();
			error = (String) xPath.compile(getExpressionForErrorCode())
												.evaluate(document, XPathConstants.STRING);
			if (error!=null && !error.isEmpty()) {
				errorResponse.setErrorCode(error);
				error = (String) xPath.compile(getExpressionForErrorMessage())
						.evaluate(document, XPathConstants.STRING);
				errorResponse.setErrorMessage(error);
			}
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
		return errorResponse;
	}	
	
	public static ErrorOnData retrieveError(String xmlDocument) {
		String error = null;
		ErrorOnData errorResponse = null;
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
			error = (String) xPath.compile(getExpressionForErrorCode())
												.evaluate(doc, XPathConstants.STRING);
			if (error!=null && !error.isEmpty()) {
				errorResponse = new ErrorOnData();
				errorResponse.setErrorCode(error);
				error = (String) xPath.compile(getExpressionForErrorMessage())
						.evaluate(doc, XPathConstants.STRING);
				errorResponse.setErrorMessage(error);
			}
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
			return null;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return errorResponse;
	}
	
	private static String getExpressionForErrorMessage() {
		return  "/OAI-PMH/"+xPathExpressionEM+"/text()";
	}
	
	private static String getExpressionForErrorCode() {
		return  "/OAI-PMH/"+xPathExpressionEC;
	}
}
