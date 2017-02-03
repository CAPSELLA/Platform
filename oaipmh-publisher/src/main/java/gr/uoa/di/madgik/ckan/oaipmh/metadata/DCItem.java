package gr.uoa.di.madgik.ckan.oaipmh.metadata;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gr.uoa.di.madgik.ckan.oaipmh.utils.XMLUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * General Dublin Core Item {@link Metadata metadata}
 *
 * @author  
 *
 */
public class DCItem extends Metadata {

    /**
     * the namespace of Dublin Core:
     * <a href='http://purl.org/dc/elements/1.1/'>http://purl.org/dc/elements/1.1/</a>
     */
    public static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1/";

    public DCItem(String prefix, String schemaLocation, String namespace) {
        super(prefix, schemaLocation, namespace);
    }

    private String title;
    private String creator;
    private List<String> subjects = new ArrayList<String>();
    private String description;
    private String publisher;
    private String contributor;
    private String date;
    private String type;
    private List<String> formats = new ArrayList<String>();
    private String identifier;
    private List<String> sources = new ArrayList<String>();
    private String language;
    private String relation;
    private String coverage;
    private String rights;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public Element getXMLElement() {
        Logger.getLogger(DCItem.class.getName()).log(Level.DEBUG, "getXMLElement");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDocument = builder.newDocument();
            Element rootElement = xmlDocument.createElementNS(this.getNamespace(),
                    getPrefix() + ":dc");
            rootElement.setAttributeNS(XMLUtils.XMLSCHEMA_INSTANCE,
                    "xsi:schemaLocation",
                    this.getNamespace() + " " + this.getSchema());

            rootElement.setAttribute("xmlns:dc", DC_NAMESPACE);

            createDCElement(rootElement, "title", title, xmlDocument);
            createDCElement(rootElement, "creator", creator, xmlDocument);

            for (String subject : subjects) {
                createDCElement(rootElement, "subject", subject, xmlDocument);
            }
            createDCElement(rootElement, "description", description, xmlDocument);
            createDCElement(rootElement, "publisher", publisher, xmlDocument);
            createDCElement(rootElement, "contributor", contributor, xmlDocument);
            createDCElement(rootElement, "date", date, xmlDocument);
            createDCElement(rootElement, "type", type, xmlDocument);

            for (String format : formats) {
                createDCElement(rootElement, "format", format, xmlDocument);
            }
            createDCElement(rootElement, "identifier", identifier, xmlDocument);

            for (String source : sources) {
                createDCElement(rootElement, "source", source, xmlDocument);
            }
            createDCElement(rootElement, "language", language, xmlDocument);
            createDCElement(rootElement, "relation", relation, xmlDocument);
            createDCElement(rootElement, "coverage", coverage, xmlDocument);
            createDCElement(rootElement, "rights", rights, xmlDocument);

            return rootElement;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DCItem.class.getName()).log(Level.ERROR, null, ex);
        }
        return null;
    }

    private static void createDCElement(Element rootElement, String name, String value, Document document) {
        Logger.getLogger(DCItem.class.getName()).log(Level.DEBUG, "createDCElement");

        if (value != null) {
            Element element = document.createElementNS(DC_NAMESPACE, "dc:"
                    + name);
            element.setTextContent(value);
            rootElement.appendChild(element);
        }
    }

}
