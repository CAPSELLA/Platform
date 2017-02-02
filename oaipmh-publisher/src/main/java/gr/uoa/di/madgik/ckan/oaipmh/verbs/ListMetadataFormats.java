package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.metadata.Metadata;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadArgumentError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.IdDoesNotExistError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoMetadataFormatsError;
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
public class ListMetadataFormats extends Verb {

    protected static Logger logger = Logger.getLogger(ListMetadataFormats.class.getName());

    private String identifier = null;
    private String requestURL;

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");
        
        super.initializeRootElement();
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setAttribute("verb", "ListMetadataFormats");
        if (identifier != null) {
            requestElement.setAttribute("identifier", identifier);
        }
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

            List<Metadata> metadataFormats = null;
            try {
                if (identifier == null) {
                    metadataFormats = repository.getMetadataFormats();
                } else {
                    metadataFormats = repository.getMetadataFormats(identifier);
                }
            } catch (NoMetadataFormatsError e) {
                addError(e);
            }catch (IdDoesNotExistError e) {
                addError(e);
            }
            if (this.hasErrors()) {
                appendErrorNodes();
            } else {

                Element listMetadataFormatsElement = xmlDocument.createElement("ListMetadataFormats");
                for (Metadata metadataFormat : metadataFormats) {
                    listMetadataFormatsElement.appendChild(xmlDocument.importNode(metadataFormat.getFormatXMLElement(), true));
                }
                rootElement.appendChild(listMetadataFormatsElement);
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
        for (Map.Entry<String, List<String>> queryParam : req.getQueryParameters().entrySet()) {
            for (String queryParamValue : queryParam.getValue()) {
                parameters.add(queryParam.getKey(), queryParamValue);
            }
        }

        if (parameters.containsKey("identifier")) {
            identifier = parameters.getFirst("identifier");
            parameters.remove("identifier");
        }

        parameters.remove("verb");
        if (parameters.size() > 0) {
            addError(new BadArgumentError());
        }
    }
}
