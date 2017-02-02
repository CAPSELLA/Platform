package gr.uoa.di.madgik.ckan.oaipmh.repository;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * the flow control that handles the {@link ResumptionToken resumptionTokens}
 *
 * @author  
 *
 */
public class FlowControl {

    private static FlowControl instance = null;
    private Map<String, ResumptionToken> resumptionTokens;

    private FlowControl() {
        resumptionTokens = new HashMap<String, ResumptionToken>();
    }

    public static synchronized FlowControl getInstance() {
        Logger.getLogger(FlowControl.class.getName()).log(Level.DEBUG, "getInstance");

        if (instance == null) {
            instance = new FlowControl();
        }
        return instance;
    }

    public synchronized boolean register(ResumptionToken resumptionToken) {
        Logger.getLogger(FlowControl.class.getName()).log(Level.DEBUG, "register");

        if (!resumptionTokens.containsKey(resumptionToken.toString())) {
            resumptionTokens.put(resumptionToken.toString(), resumptionToken);
            return true;
        }
        return false;
    }

    public synchronized boolean unregister(ResumptionToken resumptionToken) {
        Logger.getLogger(FlowControl.class.getName()).log(Level.DEBUG, "unregister");

        if (resumptionTokens.containsKey(resumptionToken.toString())) {
            resumptionTokens.remove(resumptionToken.toString());
            return true;
        }
        return false;
    }

    public synchronized boolean contains(ResumptionToken resumptionToken) {
        Logger.getLogger(FlowControl.class.getName()).log(Level.DEBUG, "contains");

        return resumptionTokens.containsKey(resumptionToken.toString());
    }

    public synchronized ResumptionToken getResumptionToken(String resumptionToken) {
        Logger.getLogger(FlowControl.class.getName()).log(Level.DEBUG, "getResumptionToken");

        if (resumptionTokens.containsKey(resumptionToken)) {
            return resumptionTokens.get(resumptionToken);
        } else {
            return null;
        }
    }

}
