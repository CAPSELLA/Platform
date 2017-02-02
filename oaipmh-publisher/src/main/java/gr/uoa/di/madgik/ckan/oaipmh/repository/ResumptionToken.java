package gr.uoa.di.madgik.ckan.oaipmh.repository;

import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author  
 *
 */
public class ResumptionToken {

    private final String resumptionToken;
    private int completeListSize;
    private UTCDatetime expirationDate;
    private int cursor;
    private String metadataPrefix = "";
    private SetSpec set = null;
    protected UTCDatetime from = null;
    protected UTCDatetime until = null;
    private Document xmlDocument;

    public ResumptionToken() {

        String resumptionToken = randomAlphaNumeric(10);
        while (FlowControl.getInstance().getResumptionToken(resumptionToken) != null) {
            resumptionToken = randomAlphaNumeric(10);
        }

        this.resumptionToken = resumptionToken;
        FlowControl.getInstance().register(this);

        this.initXML();
    }

    public ResumptionToken(String resumptionToken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return cursor;
    }

    public void setExpirationDate(UTCDatetime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UTCDatetime getExpirationDate() {
        return expirationDate;
    }

    public void setCompleteListSize(int completeListSize) {
        this.completeListSize = completeListSize;
    }

    public int getCompleteListSize() {
        return completeListSize;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public void setSet(SetSpec set) {
        this.set = set;
    }

    public void setFrom(UTCDatetime from) {
        this.from = from;
    }

    public void setUntil(UTCDatetime until) {
        this.until = until;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public SetSpec getSet() {
        return set;
    }

    public UTCDatetime getFrom() {
        return from;
    }

    public UTCDatetime getUntil() {
        return until;
    }

    public boolean isExpired() {
        ZonedDateTime datetime = ZonedDateTime.now(ZoneId.systemDefault());
        UTCDatetime current_datetime = new UTCDatetime(datetime);

        if (current_datetime.isBefore(this.expirationDate)) {
            return false;
        }
        return true;
    }

    private void initXML() {
        Logger.getLogger(ResumptionToken.class.getName()).log(Level.DEBUG, "initXML");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDocument = builder.newDocument();
        } catch (ParserConfigurationException e) {
            Logger.getLogger(ResumptionToken.class.getName()).log(Level.ERROR, null, e);

        }
    }

    public Element getXMLElement() {
        Logger.getLogger(ResumptionToken.class.getName()).log(Level.DEBUG, "getXMLElement");

        Element recordElement = xmlDocument.createElement("resumptionToken");

        recordElement.setAttribute("expirationDate", expirationDate.getDatetimeAsString());
        recordElement.setAttribute("completeListSize", String.valueOf(completeListSize));
        recordElement.setAttribute("cursor", String.valueOf(cursor));
        recordElement.setTextContent(resumptionToken);
        return recordElement;
    }

    /**
     * returns the 'resumptionToken'
     * @return 
     */
    @Override
    public String toString() {
        return resumptionToken;
    }

    private String randomAlphaNumeric(int count) {
        Logger.getLogger(ResumptionToken.class.getName()).log(Level.DEBUG, "randomAlphaNumeric");

        String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }
}
