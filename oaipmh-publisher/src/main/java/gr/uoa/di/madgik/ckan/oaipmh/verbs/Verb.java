package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.FlowControl;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.ResumptionToken;
import gr.uoa.di.madgik.ckan.oaipmh.utils.OAIPMH;
import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.ErrorCondition;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Verb {

    protected static Logger logger =  Logger.getLogger(Verb.class.getName());
    private List<ErrorCondition> errors = new ArrayList<ErrorCondition>();

    protected Document xmlDocument;

    public Verb() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDocument = builder.newDocument();
            xmlDocument.setXmlStandalone(true);
            xmlDocument.setXmlVersion("1.0");
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void initializeRootElement() {
        logger.debug("initializeRootElement");
        
        Element rootElement = xmlDocument.createElementNS(OAIPMH.OAI_NAMESPACE,"OAI-PMH");
        rootElement.setAttributeNS(XMLUtils.XMLSCHEMA_INSTANCE,
                "xsi:schemaLocation", OAIPMH.OAI_NAMESPACE + " "
                + OAIPMH.OAI_SCHEMA_LOCATION);
        Element responseDateElement = xmlDocument.createElement("responseDate");
        responseDateElement.setTextContent(UTCDatetime.now());
        rootElement.appendChild(responseDateElement);
        xmlDocument.appendChild(rootElement);
    }

    public void appendErrorNodes() {
        for (ErrorCondition error : getErrors()) {
            xmlDocument.getDocumentElement().appendChild(XMLUtils.errorToXML(error, xmlDocument));
        }
    }
    
    protected void addError(ErrorCondition error) {
        errors.add(error);
    }

    public List<ErrorCondition> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return (!errors.isEmpty());
    }

    /**
     * Handles the given {@link ResumptionToken} in the {@link FlowControl}. It
     * unregisters/registers the {@link ResumptionToken} in the
     * {@link FlowControl} depending on the current request
     *
     * @param resumptionToken
     * @param returnedListFromRepositorySize the size of the list that the
     * repository returned in the current harvesting request
     * @throws gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError
     */
    protected static void handleResumptionToken(ResumptionToken resumptionToken, int returnedListFromRepositorySize) throws BadResumptionTokenError {
        logger.debug("handleResumptionToken");
        
        FlowControl flowControl = FlowControl.getInstance();
        /*
        * if the current request is the last one, it unregisters the
        * resumptionToken
         */
        if (resumptionToken.getCursor() + returnedListFromRepositorySize >= resumptionToken.getCompleteListSize()) {
            flowControl.unregister(resumptionToken);
        } else {
            resumptionToken.setCursor(resumptionToken.getCursor() + returnedListFromRepositorySize);
        }
    }

    abstract public String response(Repository repository);

    abstract public void setAttributes(UriInfo req);
}
