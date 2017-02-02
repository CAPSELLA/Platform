package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import javax.ws.rs.core.UriInfo;

import org.w3c.dom.Element;

import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadVerbError;
import org.apache.log4j.Logger;

public class VerbError extends Verb {
    protected static Logger logger = Logger.getLogger(VerbError.class.getName());

    protected String requestURL;

    @Override
    public String response(Repository repository) {
        logger.debug("response");
        
        initializeRootElement();

        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setTextContent(requestURL);
        rootElement.appendChild(requestElement);

        if (this.hasErrors()) {
            appendErrorNodes();
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
        addError(new BadVerbError(req.getQueryParameters().getFirst("verb")));

    }

}
