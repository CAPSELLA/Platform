package gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo;

import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.AdditionalInfoForRepo;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import gr.uoa.di.madgik.datatransformation.harvester.utils.RequestData;

public class RetrieveAdditionalInfo {
	private final static Logger logger = Logger.getLogger(RequestData.class);
	
	public static AdditionalInfoForRepo retrieveAdditionalInfoOfRepo(String xmlFile) {
		AdditionalInfoForRepo additionalInfo = new AdditionalInfoForRepo();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new StringBufferInputStream(xmlFile));
			XPath xPath = XPathFactory.newInstance().newXPath();
			additionalInfo.setEarliestDatestamp((String) xPath.compile(getEarliestDatestamp())
												.evaluate(document, XPathConstants.STRING));
			additionalInfo.setGranularity((String) xPath.compile(getGranularity())
					.evaluate(document, XPathConstants.STRING));
			additionalInfo.setRepositoryName((String) xPath.compile(getRepositoryName())
					.evaluate(document, XPathConstants.STRING));
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
		return additionalInfo;
	}	
	
	private static String getEarliestDatestamp() {
		return  "/OAI-PMH/Identify/earliestDatestamp/text()";
	}
	
	private static String getRepositoryName() {
		return  "/OAI-PMH/Identify/repositoryName/text()";
	}
	
	private static String getGranularity() {
		return  "/OAI-PMH/Identify/granularity/text()";
	}
	
}