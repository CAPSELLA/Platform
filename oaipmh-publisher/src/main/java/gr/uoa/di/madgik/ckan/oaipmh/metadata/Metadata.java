package gr.uoa.di.madgik.ckan.oaipmh.metadata;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Metadata {

    private final String prefix;
    private final String schema;
    private final String namespace;

    public Metadata(String prefix, String schema, String namespace) {
        this.prefix = prefix;
        this.schema = schema;
        this.namespace = namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getSchema() {
        return schema;
    }

    public Element getFormatXMLElement() {
        Logger.getLogger(Metadata.class.getName()).log(Level.DEBUG, "getFormatXMLElement");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDocument = builder.newDocument();

            Element metadataElement = xmlDocument
                    .createElement("metadataFormat");
            Element prefixElement = xmlDocument.createElement("metadataPrefix");
            prefixElement.setTextContent(prefix);
            metadataElement.appendChild(prefixElement);
            Element schemaElement = xmlDocument.createElement("schema");
            schemaElement.setTextContent(schema);
            metadataElement.appendChild(schemaElement);
            Element namespaceElement = xmlDocument
                    .createElement("metadataNamespace");
            namespaceElement.setTextContent(namespace);
            metadataElement.appendChild(namespaceElement);

            return metadataElement;
        } catch (ParserConfigurationException e) {
            Logger.getLogger(Metadata.class.getName()).log(Level.ERROR, null, e);
        }
        return null;
    }

    public abstract Element getXMLElement() throws Exception;
}
