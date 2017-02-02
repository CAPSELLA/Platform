package gr.uoa.di.madgik.ckan.oaipmh.metadata;

import gr.uoa.di.madgik.ckan.oaipmh.utils.OAIPMH;

/**
 * OAI {@link DCItem dublin core item}
 *
 * @author 
 *
 */
public class OAIDCItem extends DCItem {

    public OAIDCItem() {
        super("oai_dc", OAIPMH.OAI_SCHEMA_LOCATION, OAIPMH.OAI_NAMESPACE);
    }

}
