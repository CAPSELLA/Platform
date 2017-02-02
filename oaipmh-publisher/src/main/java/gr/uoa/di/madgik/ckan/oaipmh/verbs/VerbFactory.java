package gr.uoa.di.madgik.ckan.oaipmh.verbs;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Ioannis Kavvouras
 *
 */
public class VerbFactory {

    private VerbFactory() {

    }

    public static Verb getVerbFactoryMethod(String verb) {
        Logger.getLogger(VerbFactory.class.getName()).log(Level.DEBUG, "getVerbFactoryMethod");

        if (verb == null) {
            return new BadVerb();
        }
        if (verb.equals("GetRecord")) {
            return new GetRecord();
        } else if (verb.equals("Identify")) {
            return new Identify();
        } else if (verb.equals("ListIdentifiers")) {
            return new ListIdentifiers();
        } else if (verb.equals("ListMetadataFormats")) {
            return new ListMetadataFormats();
        } else if (verb.equals("ListRecords")) {
            return new ListRecords();
        } else if (verb.equals("ListSets")) {
            return new ListSets();
        } else {
            return new BadVerb();
        }
    }
}
