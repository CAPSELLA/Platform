package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadVerbError;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author  
 */
public class BadVerb extends Verb {

    protected static Logger logger = Logger.getLogger(GetRecord.class.getName());

    private String requestURL;

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");

        super.initializeRootElement();
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setTextContent(requestURL);
        rootElement.appendChild(requestElement);
    }

    @Override
    public String response(Repository repository) {
        logger.debug("response");

        initializeRootElement();

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

        addError(new BadVerbError("Illegal OAI verb"));

    }

}
