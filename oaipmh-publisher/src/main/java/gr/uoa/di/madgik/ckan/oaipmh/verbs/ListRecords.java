package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import gr.uoa.di.madgik.ckan.oaipmh.repository.FlowControl;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Record;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.RepositoryRegistrationException;
import gr.uoa.di.madgik.ckan.oaipmh.repository.ResumptionToken;
import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;

import java.util.List;
import org.apache.log4j.Logger;

import org.w3c.dom.Element;
/**
 *
 * @author  
 */
public class ListRecords extends ListIdentifiers {

    protected static Logger logger = Logger.getLogger(ListRecords.class.getName());

    @Override
    public void initializeRootElement() {
        logger.debug("initializeRootElement");
                
        super.initializeRootElement();

        Element rootElement = xmlDocument.getDocumentElement();
        Element requestElement = xmlDocument.createElement("request");

        requestElement.setAttribute("verb", "ListRecords");
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

        initializeRootElement();

        if (this.hasErrors()) {
            appendErrorNodes();
        } else {

            Element rootElement = xmlDocument.getDocumentElement();
            List<Record> records;
            try {
                records = getRecords(repository);
            } catch (RepositoryRegistrationException e) {
                logger.error(e.getMessage(), e);
                records = null;
            }

            if (this.hasErrors()) {
                appendErrorNodes();
            } else {
                Element listIdentifiersElement = xmlDocument.createElement("ListRecords");
                for (Record record : records) {
                    listIdentifiersElement.appendChild(xmlDocument.importNode(record.getXMLElement(), true));
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
                            logger.info(ex);
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

}
