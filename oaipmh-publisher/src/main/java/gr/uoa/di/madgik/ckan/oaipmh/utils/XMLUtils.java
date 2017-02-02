package gr.uoa.di.madgik.ckan.oaipmh.utils;

import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.ErrorCondition;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author  
 *
 */

public class XMLUtils {

    protected static Logger logger = Logger.getLogger(XMLUtils.class.getName());
    public static final String XMLSCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

    private XMLUtils() {
    }

    public static Element errorToXML(ErrorCondition error, Document document) {
        Element element = document.createElement("error");
        element.setAttribute("code", error.getCode());
        if ((error.getMessage() != null) && (!error.getMessage().isEmpty())) {
            element.setTextContent(error.getMessage());
        }
        return element;
    }

    public static String transformDocumentToString(Document document) throws Exception {
        return nodeToString(document);
    }

    public static String transformDocumentToString(Element document) throws Exception {
        return nodeToString(document);
    }

    public static String nodeToString(Node node) throws Exception {
        logger.debug("nodeToString");
        StringWriter sw = new StringWriter();
        try {

            TransformerFactory fact = TransformerFactory.newInstance();
            Transformer t = fact.newTransformer();

            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            logger.error("Error during XML to string conversion", te);
            throw new Exception("Error during XML to string conversion", te);
        }
        return sw.toString();
    }

}
