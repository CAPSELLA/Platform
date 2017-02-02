package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.utils.OAIPMH;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadArgumentError;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

import org.w3c.dom.Element;

/**
 *
 * @author  
 */
public class Identify extends Verb {

    protected static Logger logger = Logger.getLogger(Identify.class.getName());

    private String requestURL;

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");

        super.initializeRootElement();
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setAttribute("verb", "Identify");
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
            Element identifyElement = xmlDocument.createElement("Identify");
            Element repositoryNameElement = xmlDocument
                    .createElement("repositoryName");
            repositoryNameElement
                    .setTextContent(repository.getRepositoryName());
            identifyElement.appendChild(repositoryNameElement);
            Element baseUrlElement = xmlDocument.createElement("baseURL");
            baseUrlElement.setTextContent(repository.getRequestURL());
            identifyElement.appendChild(baseUrlElement);
            Element protocolVersionElement = xmlDocument
                    .createElement("protocolVersion");
            protocolVersionElement.setTextContent(OAIPMH.VERSION);
            identifyElement.appendChild(protocolVersionElement);
            Element earliestDatestampElement = xmlDocument
                    .createElement("earliestDatestamp");
            earliestDatestampElement.setTextContent(repository
                    .getEarliestDatestamp());
            identifyElement.appendChild(earliestDatestampElement);
            Element deletedRecordElement = xmlDocument
                    .createElement("deletedRecord");
            deletedRecordElement.setTextContent(repository.getDeletedRecord());
            identifyElement.appendChild(deletedRecordElement);
            Element granularityElement = xmlDocument
                    .createElement("granularity");
            granularityElement.setTextContent(repository.getGranularity());
            identifyElement.appendChild(granularityElement);
            for (String email : repository.getAdminEmails()) {
                Element emailElement = xmlDocument.createElement("adminEmail");
                emailElement.setTextContent(email);
                identifyElement.appendChild(emailElement);
            }
            if (repository.getCompression() != null) {
                Element compressionElement = xmlDocument
                        .createElement("compression");
                compressionElement.setTextContent(repository.getCompression());
                identifyElement.appendChild(compressionElement);
            }
            if (repository.getDescriptions() != null) {
                for (String descr : repository.getDescriptions()) {
                    Element descriptionElement = xmlDocument
                            .createElement("description");
                    descriptionElement.setTextContent(descr);
                    identifyElement.appendChild(descriptionElement);
                }
            }
            rootElement.appendChild(identifyElement);
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
        for (Map.Entry<String, List<String>> queryParam : req.getQueryParameters().entrySet()) {
            for (String queryParamValue : queryParam.getValue()) {
                parameters.add(queryParam.getKey(), queryParamValue);
            }
        }
        parameters.remove("verb");
        if (parameters.size() > 0) {
            addError(new BadArgumentError("There are extra parameters"));
        }
    }

}
