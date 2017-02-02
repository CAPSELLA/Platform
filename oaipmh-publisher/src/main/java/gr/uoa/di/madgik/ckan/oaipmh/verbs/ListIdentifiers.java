package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.FlowControl;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Record;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.RepositoryRegistrationException;
import gr.uoa.di.madgik.ckan.oaipmh.repository.ResumptionToken;
import gr.uoa.di.madgik.ckan.oaipmh.repository.SetSpec;
import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadArgumentError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.CannotDisseminateFormatError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoRecordsMatchError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoSetHierarchyError;

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
public class ListIdentifiers extends Verb {
    
    protected static Logger logger = Logger.getLogger(ListIdentifiers.class.getName());
    protected UTCDatetime from = null;
    protected UTCDatetime until = null;
    protected String metadataPrefix;
    protected SetSpec set = null;
    protected String resumptionToken = null;
    
    protected String requestURL;
    
    public void setResumptionToken(String resumptionToken) {
        this.resumptionToken = resumptionToken;
    }
    
    public void localInitializeRootElement() {
        logger.debug("localInitializeRootElement");
        
        super.initializeRootElement();
        
        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");
        requestElement.setAttribute("verb", "ListIdentifiers");
        if (metadataPrefix != null) {
            requestElement.setAttribute("metadataPrefix", metadataPrefix);
        }
        if (from != null) {
            requestElement.setAttribute("from", from.getDatetimeAsString());
        }
        if (until != null) {
            requestElement.setAttribute("until", until.getDatetimeAsString());
        }
        if (set != null) {
            requestElement.setAttribute("set", set.toString());
        }
        if (resumptionToken != null) {
            requestElement.setAttribute("resumptionToken", resumptionToken);

            //get all information from resumption token
            FlowControl flowControl = FlowControl.getInstance();
            ResumptionToken token = flowControl.getResumptionToken(resumptionToken);
            
            metadataPrefix = token.getMetadataPrefix();
            from = token.getFrom();
            until = token.getUntil();
            set = token.getSet();
        }
        requestElement.setTextContent(requestURL);
        rootElement.appendChild(requestElement);
    }
    
    @Override
    public String response(Repository repository) {
        logger.debug("response");
        
        localInitializeRootElement();
        
        if (this.hasErrors()) {
            appendErrorNodes();
        } else {
            Element rootElement = xmlDocument.getDocumentElement();
            
            List<Record> records;
            try {
                records = getRecords(repository);
            } catch (RepositoryRegistrationException e1) {
                logger.error(e1);
                //e1.printStackTrace();
                records = null;
            }
            
            if (this.hasErrors()) {
                appendErrorNodes();
            } else {
                Element listIdentifiersElement = xmlDocument.createElement("ListIdentifiers");
                for (Record record : records) {
                    listIdentifiersElement.appendChild(xmlDocument.importNode(record.getHeaderAsXMLElement(), true));
                }

                //add resumption token in xml element if exists
                if (resumptionToken != null) {
                    FlowControl flowControl = FlowControl.getInstance();
                    ResumptionToken token = flowControl.getResumptionToken(resumptionToken);
                    if (token != null) {
                        listIdentifiersElement.appendChild(xmlDocument.importNode(token.getXMLElement(), true));
                        
                        try {
                            Verb.handleResumptionToken(token, records.size());
                        } catch (BadResumptionTokenError ex) {
                            this.addError(ex);
                            logger.error(ex);
                        }
                        
                        if (token.getMetadataPrefix().equals("")) {
                            
                            token.setFrom(from);
                            token.setMetadataPrefix(metadataPrefix);
                            token.setSet(set);
                            token.setUntil(until);
                        }
                    }
                }
                rootElement.appendChild(listIdentifiersElement);
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
        } else {
            if (parameters.containsKey("from")) {
                from = new UTCDatetime(parameters.getFirst("from"));
                parameters.remove("from");
            }
            if (parameters.containsKey("until")) {
                until = new UTCDatetime(parameters.getFirst("until"));
                parameters.remove("until");
            }
            if (parameters.containsKey("metadataPrefix")) {
                metadataPrefix = parameters.getFirst("metadataPrefix");
                parameters.remove("metadataPrefix");
            } else {
                addError(new BadArgumentError("There was no metadataPrefix"));
                return;
            }
            if (parameters.containsKey("set")) {
                set = new SetSpec(parameters.getFirst("set"));
                parameters.remove("set");
            }
        }
        parameters.remove("verb");
        if (parameters.size() > 0) {
            addError(new BadArgumentError("There are extra parameters"));
        }
    }
    
    protected List<Record> getRecords(Repository repository) throws RepositoryRegistrationException {
        logger.debug("getRecords");
        try {
            if ((until == null) && (from == null) && (set == null) && (resumptionToken == null)) {
                return repository.getRecords(metadataPrefix, this);
            } else if ((until != null) || (from != null) && (set == null) && (resumptionToken == null)) {
                return repository.getRecords(from, until, metadataPrefix);
            } else if ((until == null) && (from == null) && (set != null) && (resumptionToken == null)) {
                return repository.getRecords(metadataPrefix, set, this);
            } else if ((until == null) && (from == null) && (set == null) && (resumptionToken != null)) {
                return repository.getRecords(metadataPrefix, FlowControl.getInstance().getResumptionToken(resumptionToken));
            } else if ((until == null) && (from == null) && (set != null) && (resumptionToken != null)) {
                return repository.getRecords(metadataPrefix, FlowControl.getInstance().getResumptionToken(resumptionToken), set);
            } else {
                return repository.getRecords(from, until, metadataPrefix, FlowControl.getInstance().getResumptionToken(resumptionToken), set);
            }
        } catch (CannotDisseminateFormatError e) {
            addError(e);
            logger.info("ErrorCondition: " + e.getCode());
        } catch (NoRecordsMatchError e) {
            addError(e);
            logger.info("ErrorCondition: " + e.getCode());
        } catch (NoSetHierarchyError e) {
            addError(e);
            logger.info("ErrorCondition: " + e.getCode());
        } catch (BadResumptionTokenError e) {
            addError(e);
            logger.info("ErrorCondition: " + e.getCode());
        }
        return null;
    }
}
