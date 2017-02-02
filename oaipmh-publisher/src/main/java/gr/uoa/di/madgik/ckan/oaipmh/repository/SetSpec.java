package gr.uoa.di.madgik.ckan.oaipmh.repository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gr.uoa.di.madgik.ckan.oaipmh.metadata.DCItem;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author  
 *
 */
public class SetSpec {

    private String setSpec;
    private String setName;
    private DCItem setDescription = null;

    public SetSpec(String setSpec) {
        this.setSpec = setSpec;
    }

    public SetSpec(String setSpec, String setName) {
        this.setSpec = setSpec;
        this.setName = setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setSetDescription(DCItem setDescription) {
        this.setDescription = setDescription;
    }

    public String getSetSpec() {
        return setSpec;
    }

    /**
     * returns the 'setSpec' value
     */
    @Override
    public String toString() {
        return setSpec;
    }

    public Element getXMLElement() throws ParserConfigurationException {
        Logger.getLogger(SetSpec.class.getName()).log(Level.DEBUG, "getXMLElement");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.newDocument();
        Element setElement = xmlDocument.createElement("set");
        Element specElement = xmlDocument.createElement("setSpec");
        specElement.setTextContent(setSpec);
        setElement.appendChild(specElement);
        Element nameElement = xmlDocument.createElement("setName");
        nameElement.setTextContent(setName);
        setElement.appendChild(nameElement);
        if (setDescription != null) {
            Element descrElement = xmlDocument.createElement("setDescription");
            descrElement.appendChild(xmlDocument.importNode(
                    setDescription.getXMLElement(), true));
            setElement.appendChild(descrElement);
        }
        return setElement;
    }
}
