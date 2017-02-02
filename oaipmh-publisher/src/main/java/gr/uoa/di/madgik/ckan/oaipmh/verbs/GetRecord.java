package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.Record;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadArgumentError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.CannotDisseminateFormatError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.IdDoesNotExistError;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

import org.w3c.dom.Element;

/**
 *
 * @author  
 */
public class GetRecord extends Verb {

    protected static Logger logger = Logger.getLogger(GetRecord.class.getName());

    private String requestURL;
    private String identifier;
    private String metadataPrefix;

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");

        super.initializeRootElement();
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setAttribute("verb", "GetRecord");
        requestElement.setAttribute("identifier", identifier);
        requestElement.setAttribute("metadataPrefix", metadataPrefix);
        requestElement.setTextContent(requestURL);
        rootElement.appendChild(requestElement);
    }

    @Override
    public String response(Repository repository) {
        logger.debug("response");

        initializeRootElement();

        if (this.hasErrors()) {
            appendErrorNodes();
        } else {

            Element rootElement = xmlDocument.getDocumentElement();

            Element getRecordElement = xmlDocument.createElement("GetRecord");
            try {
                Record record = repository.getRecord(identifier, metadataPrefix);
                getRecordElement.appendChild(xmlDocument.importNode(record.getXMLElement(), true));
                rootElement.appendChild(getRecordElement);
            } catch (IdDoesNotExistError e) {
                rootElement.appendChild(XMLUtils.errorToXML(e, xmlDocument));
                logger.info("ErrorCondition: " + e.getCode());
            } catch (CannotDisseminateFormatError e) {
                rootElement.appendChild(XMLUtils.errorToXML(e, xmlDocument));
                logger.info("ErrorCondition: " + e.getCode());
            }
        }

        try {
            return XMLUtils.transformDocumentToString(xmlDocument);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void setAttributes(UriInfo req) {
        logger.debug("setAttributes");
        
        requestURL = req.getAbsolutePath().toString();

        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        for (Entry<String, List<String>> queryParam : req.getQueryParameters().entrySet()) {
            for (String queryParamValue : queryParam.getValue()) {
                parameters.add(queryParam.getKey(), queryParamValue);
            }
        }
        if (parameters.containsKey("metadataPrefix")) {
            metadataPrefix = parameters.getFirst("metadataPrefix");
            parameters.remove("metadataPrefix");
        } else {
            addError(new BadArgumentError("There was no metadataPrefix"));
            return;
        }
        if (parameters.containsKey("identifier")) {
            identifier = parameters.getFirst("identifier");
            parameters.remove("identifier");
        } else {
            addError(new BadArgumentError("There was no identifier"));
            return;
        }
        parameters.remove("verb");
        if (parameters.size() > 0) {
            addError(new BadArgumentError("There are extra parameters"));
        }
    }

}
