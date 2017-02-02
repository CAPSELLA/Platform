package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.FlowControl;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.ResumptionToken;
import gr.uoa.di.madgik.ckan.oaipmh.repository.SetSpec;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadArgumentError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoSetHierarchyError;

import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class ListSets extends Verb {

    protected static Logger logger = Logger.getLogger(ListSets.class.getName());

    private String resumptionToken = null;
    private String requestURL;

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");

        super.initializeRootElement();
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setAttribute("verb", "ListSets");
        if (resumptionToken != null) {
            requestElement.setAttribute("resumptionToken", resumptionToken);
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

            List<SetSpec> setSpecs = null;

            try {
                if (resumptionToken == null) {
                    setSpecs = repository.getSetSpecs();
                } else {
                    setSpecs = repository.getSetSpecs(FlowControl.getInstance().getResumptionToken(resumptionToken));
                }
            } catch (NoSetHierarchyError e) {
                addError(e);
            } catch (BadResumptionTokenError e) {
                addError(e);
            }

            if (this.hasErrors()) {
                appendErrorNodes();
            } else {

                Element listSetsElement = xmlDocument.createElement("ListSets");
                for (SetSpec setSpec : setSpecs) {
                    try {
                        listSetsElement.appendChild(xmlDocument.importNode(
                                setSpec.getXMLElement(), true));
                    } catch (DOMException e) {
                        logger.error(e.getMessage(), e);
                    }catch (ParserConfigurationException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                //add resumption token in xml element if exists
                if (resumptionToken != null) {
                    FlowControl flowControl = FlowControl.getInstance();
                    ResumptionToken token = flowControl.getResumptionToken(resumptionToken);
                    if (token != null) {
                        listSetsElement.appendChild(xmlDocument.importNode(token.getXMLElement(), true));

                        try {
                            Verb.handleResumptionToken(token, setSpecs.size());
                        } catch (BadResumptionTokenError ex) {
                            this.addError(ex);
                            logger.info(ex);
                        }
                    }
                }
                rootElement.appendChild(listSetsElement);
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

        if (parameters.containsKey("resumptionToken")) {
            resumptionToken = parameters.getFirst("resumptionToken");
            ResumptionToken token = FlowControl.getInstance().getResumptionToken(resumptionToken);
            if (token == null) {
                addError(new BadResumptionTokenError("resumtionToken does not exist"));
                resumptionToken = null;
                return;
            } else if (token.isExpired()) {
                FlowControl.getInstance().unregister(token);
                addError(new BadResumptionTokenError("resumtionToken has expired"));
                resumptionToken = null;
                return;
            }
            parameters.remove("resumptionToken");
        }

        parameters.remove("verb");
        if (parameters.size() > 0) {
            addError(new BadArgumentError());
        }
    }

}
