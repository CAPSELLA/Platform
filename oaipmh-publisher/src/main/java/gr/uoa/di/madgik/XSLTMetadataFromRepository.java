package gr.uoa.di.madgik;

import gr.uoa.di.madgik.ckan.oaipmh.metadata.Metadata;
import gr.uoa.di.madgik.ckan.oaipmh.utils.OAIPMH;
import org.w3c.dom.Element;

/**
 *
 * @author  
 */

public class XSLTMetadataFromRepository extends Metadata {

	private final Element xmlElement;

	public XSLTMetadataFromRepository(Element xmlElement) {
		super("oai_dc", OAIPMH.OAI_SCHEMA_LOCATION, OAIPMH.OAI_NAMESPACE);

		this.xmlElement = xmlElement;
	}

	@Override
	public Element getXMLElement() throws Exception {
		return xmlElement;
	}
	

}
