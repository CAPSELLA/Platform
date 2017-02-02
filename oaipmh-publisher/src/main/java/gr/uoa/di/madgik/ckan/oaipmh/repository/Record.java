package gr.uoa.di.madgik.ckan.oaipmh.repository;

import gr.uoa.di.madgik.ckan.oaipmh.metadata.Metadata;
import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  
 *
 */
public class Record {

    protected static Logger logger = Logger.getLogger(Record.class.getName());

    private final String id;
    private final String metadataPrefix;
    private Metadata metadata;
    private UTCDatetime datetime;
    private boolean isDeleted = false;
    private List<SetSpec> setSpecs = new ArrayList<SetSpec>();

    private Document xmlDocument;

    public Element aboutElement = null;

    public Record(String id, String metadataPrefix) {
        this.id = id;
        this.metadataPrefix = metadataPrefix;
        initXML();
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String getId() {
        return id;
    }

    public List<SetSpec> getSetSpecs() {
        return setSpecs;
    }

    public void setSetSpecs(List<SetSpec> setSpecs) {
        this.setSpecs = setSpecs;
    }

    public UTCDatetime getDatetime() {
        if (datetime == null) {
            return datetime = new UTCDatetime(UTCDatetime.now());
        }
        return datetime;
    }

    public void setDatetime(UTCDatetime datetime) {
        this.datetime = datetime;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    private void initXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDocument = builder.newDocument();
        } catch (ParserConfigurationException e) {
            logger.error(e);
        }
    }

    public Element getXMLElement() {

        logger.debug("getXMLElement");
        Element recordElement = xmlDocument.createElement("record");

        recordElement.appendChild(getHeaderAsXMLElement());
        Element metadataElement = xmlDocument.createElement("metadata");
        try {
            metadataElement.appendChild(xmlDocument.importNode(
                    metadata.getXMLElement(), true));
        } catch (DOMException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        recordElement.appendChild(metadataElement);
        if (aboutElement != null) {
            recordElement.appendChild(aboutElement);
        }
        return recordElement;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Element getHeaderAsXMLElement() {
        logger.debug("getHeaderAsXMLElement");
        Element headerElement = xmlDocument.createElement("header");
        if (isDeleted) {
            headerElement.setAttribute("status", "deleted");
        }
        Element idElement = xmlDocument.createElement("identifier");
        idElement.setTextContent(id);
        headerElement.appendChild(idElement);
        Element datestampElement = xmlDocument.createElement("datestamp");
        datestampElement.setTextContent(getDatetime().getDatetimeAsString());
        headerElement.appendChild(datestampElement);

        for (SetSpec setSpec : setSpecs) {
            Element setElement = xmlDocument.createElement("setSpec");
            setElement.setTextContent(setSpec.toString());
            headerElement.appendChild(setElement);
        }

        return headerElement;
    }
    
    
}
