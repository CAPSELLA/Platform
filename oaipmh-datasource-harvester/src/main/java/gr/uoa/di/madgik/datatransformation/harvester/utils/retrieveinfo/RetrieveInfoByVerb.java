package gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo;



import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.RetrievedNodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class RetrieveInfoByVerb {       
	private final static Logger logger = Logger.getLogger(RetrieveInfoByVerb.class);

	public static RetrievedNodes getNodes(String xml) throws ParseException {
		// contains all records -- records with metadata and records like the bottom:
		/*
		 * <record>
		 * 		<header status="deleted">
      	 *			<identifier>oai:www.ucm.es:1416</identifier>
      	 *			<datestamp>2008-07-08T13:58:39Z</datestamp>
      	 *		</header>
      	 * </record>
		 * */
		NodeList nodes = RetrieveInfoByVerb.retrieveNodes(xml);
		
		RetrievedNodes retrievedNodes = new RetrievedNodes();
		
		if (nodes!=null && nodes.getLength()>0) {
	        for (int i=0; i<nodes.getLength(); i++) {
	            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Node header = ((Element) nodes.item(i)).getElementsByTagName("header").item(0);
					
					Element element = (Element) header;
					
					if (element.getAttribute("status")!=null && !element.getAttribute("status").isEmpty())
						retrievedNodes.addToNodesToDelete(((Element)header).getElementsByTagName("identifier").item(0).getTextContent());
					else retrievedNodes.addToNodes(((Element)header).getElementsByTagName("identifier").item(0).getTextContent(), nodes.item(i));
	            }
	        }
	    }
		return retrievedNodes;
	}
	
	public static NodeList retrieveNodes(String xml) {
		NodeList nodes = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			
			Document document = db.parse(is);
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			nodes = (NodeList) xPath.compile("/OAI-PMH/ListRecords/record").evaluate(document, XPathConstants.NODESET);
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.info(e.getMessage());
		} catch (XPathExpressionException e) {
			logger.info(e.getMessage());
		} catch (SAXException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return nodes; 
	}
	
	
	private static NodeList getNodesOfListRecords(String xmlFile) throws ParseException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		NodeList nodes = null;
		try {
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlFile));
			Document document = builder.parse(is);
			XPath xPath = XPathFactory.newInstance().newXPath();
			nodes = (NodeList) xPath.compile(getInfo("ListRecords")).evaluate(document, XPathConstants.NODESET);
		} catch (NullPointerException e ) {
			logger.info(e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.info(e.getMessage());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return nodes; 
	}
	
	public static boolean getNodesFromBeforeLeastDate(String xmlFile, Date beforeLeastRecentlyDate) throws ParseException {
		NodeList nodes = getNodesOfListRecords(xmlFile);
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Node header = ((Element) node).getElementsByTagName("header").item(0);
			String datestamp = ((Element)header).getElementsByTagName("datestamp").item(0).getTextContent();
			Date date = javax.xml.bind.DatatypeConverter.parseDateTime(datestamp).getTime();
			if (date.equals(beforeLeastRecentlyDate) || date.before(beforeLeastRecentlyDate)) return false;
		}
		return true;
	}

	private static String getInfo(String verb) {
		if (verb.equals("ListRecords"))
			return  "/OAI-PMH/" + verb + "/record";
		else if (verb.equals("ListSets"))
			return  "/OAI-PMH/" + verb + "/set";
		else if (verb.equals("ListMetadataFormats"))
			return  "/OAI-PMH/" + verb + "/metadataFormat";
		else if (verb.equals("ListIdentifiers"))
			return  "/OAI-PMH/" + verb + "/header";
		else if (verb.equals("Identify"))
			return  "/OAI-PMH/" + verb;
		else if (verb.equals("GetRecord"))
			return  "/OAI-PMH/" + verb;
	
		else return null;
	}
	
	public static String createNewXML(String verb, List<NodeList> listNodeLists, List<Node> listNodes) {
		Document newXML = null;
		try {
			newXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = newXML.createElement(verb);
			newXML.appendChild(root);
			
			for (NodeList nodes: listNodeLists) {
				for (int i=0; i<nodes.getLength(); i++) {
					Node node = nodes.item(i);
					Node copyNode = newXML.importNode(node, true);
					root.appendChild(copyNode);				
					listNodes.add(node);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
		if (newXML!=null)
			return printXmlDocument(newXML);
		else return null;
	}
	
	public static class LeastDates {
		
		private Date leastRecentlyDate = null;
		private Date beforeLeastRecentlyDate = null;
		
		public Date getLeastRecentlyDate() {
			return leastRecentlyDate;
		}
		public void setLeastRecentlyDate(Date leastRecentlyDate) {
			this.leastRecentlyDate = leastRecentlyDate;
		}
		
		public Date getBeforeLeastRecentlyDate() {
			return beforeLeastRecentlyDate;
		}
		public void setBeforeLeastRecentlyDate(Date beforeLeastRecentlyDate) {
			this.beforeLeastRecentlyDate = beforeLeastRecentlyDate;
		} 
	
	}

	public static String printXmlDocument(Document document) {
	    DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation();
	    LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
	    
	    LSOutput lsOutput = domImplementationLS.createLSOutput();
	    lsOutput.setEncoding("UTF-8");
	    Writer stringWriter = new StringWriter();
	    lsOutput.setCharacterStream(stringWriter);
	    lsSerializer.write(document, lsOutput);
	    
	    String string = stringWriter.toString();
	    return string;
	}
	
}