/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author anatol
 */
public class ADOM {
//---------------------------------------------------------------------------

    /**
     * Создаёт DocumentBuilder.
     *
     * @return экземпляр DocumentBuilder
     * @throws AException в случае ошибки
     */
    private static DocumentBuilder getBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderfactory = DocumentBuilderFactory.newInstance();
        docBuilderfactory.setNamespaceAware(true);
        docBuilderfactory.setValidating(false);
        return docBuilderfactory.newDocumentBuilder();
    }
//---------------------------------------------------------------------------

    /**
     * Воссоздаёт документ из потока in с данными XML.
     *
     * @param in поток с данными XML
     * @return документ с данными XML
     * @throws java.io.IOException в случае ошибки
     * @throws javax.xml.parsers.ParserConfigurationException в случае ошибки
     * @throws org.xml.sax.SAXException в случае ошибки
     */
    public static Document getDocument(InputStream in) throws IOException, ParserConfigurationException, SAXException {
        return getBuilder().parse(in);
    }
//---------------------------------------------------------------------------

    /**
     * Воссоздаёт документ из байтового массива xml с данными XML.
     *
     * @param xml байтовый массив с данными XML
     * @return документ с данными XML
     * @throws java.io.IOException в случае ошибки
     * @throws javax.xml.parsers.ParserConfigurationException в случае ошибки
     * @throws org.xml.sax.SAXException в случае ошибки
     */
    public static Document getDocument(byte[] xml) throws IOException, ParserConfigurationException, SAXException {
        return getDocument(new ByteArrayInputStream(xml));
    }
//---------------------------------------------------------------------------

    /**
     * Воссоздаёт документ из строки xml с данными XML.<br/>
     *
     * @param xml строка с данными XML в формате UTF-8
     * @return документ с данными XML
     * @throws AException в случае ошибки
     */
    public static Document getDocument(String xml) throws UnsupportedEncodingException, IOException, ParserConfigurationException, SAXException {
        return getDocument(xml.getBytes("UTF-8"));
    }
//---------------------------------------------------------------------------

    /**
     * Создаёт пустой документ.<br/>
     *
     * @return пустой документ
     * @throws AException
     */
    public static Document createDocument() throws ParserConfigurationException {
        return getBuilder().newDocument();
    }
//---------------------------------------------------------------------------

    public static String nodeToStr(Node node) throws TransformerException {
        if (node == null) {
            throw new IllegalArgumentException("node = null");
        }
        DOMSource domSource = new DOMSource(node);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }
//---------------------------------------------------------------------------

    public static Node findNodeList(String nodeName, NodeList nodeList) {
        if (nodeName == null) {
            throw new IllegalArgumentException("nodeName = null");
        }
        Node nodeFound = null;
        for (int q = 0; q < nodeList.getLength(); q++) {
            Node node = nodeList.item(q);
            if (node == null) {
                return null;
            }
            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            String localName = node.getLocalName();
            if (localName != null) {
                if (localName.equals(nodeName)) {
                    return node;
                }
            }
            if (node.hasChildNodes()) {
                nodeFound = findNodeList(nodeName, node.getChildNodes());
            }
            if (nodeFound != null) {
                break;
            }
        }
        return nodeFound;
    }
//---------------------------------------------------------------------------

    public static Node findFirstNode(String nodeName, NodeList nodeList) {
        Node nodeFound = null;
        for (int q = 0; q < nodeList.getLength(); q++) {
            Node node = nodeList.item(q);
            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            if (node.getLocalName().equals(nodeName)) {
                return node;
            }
            if (node.hasChildNodes()) {
                nodeFound = findNodeList(nodeName, node.getChildNodes());
            }
            if (nodeFound != null) {
                break;
            }
        }
        return nodeFound;
    }
//---------------------------------------------------------------------------

    public static Node findLastNode(String nodeName, NodeList nodeList) {
        Node nodeFound = null;
        for (int q = 0; q < nodeList.getLength(); q++) {
            Node node = nodeList.item(q);
            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }
            if (node.getLocalName().equals(nodeName)) {
                return node;
            }
            if (node.hasChildNodes()) {
                nodeFound = findNodeList(nodeName, node.getChildNodes());
            }
            if (nodeFound != null) {
                break;
            }
        }
        return nodeFound;
    }
//---------------------------------------------------------------------------

    public static Node firstChild(Node source, boolean required) {
        if (source == null) {
            throw new IllegalArgumentException("source = null");
        }
        if (source.hasChildNodes()) {
            NodeList nodeList = source.getChildNodes();
            for (int q = 0; q < nodeList.getLength(); q++) {
                Node node = nodeList.item(q);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    return node;
                }
            }
        }
        if (required) {
            throw new IllegalArgumentException("source has not have child node");
        }
        return null;
    }
//---------------------------------------------------------------------------

    public static Node getNode(Document doc, String nodeName) {
        return findFirstNode(nodeName, doc.getChildNodes());
    }
//---------------------------------------------------------------------------

    public static String getNodeValue(Document doc, String nodeName) {
        try {
            if (!doc.hasChildNodes()) {
                return null;//???
            }
            Node node = findFirstNode(nodeName, doc.getChildNodes());
            if (node == null) {
                return null;
            }
            return node.getTextContent();
        } catch (DOMException ex) {
            throw new IllegalArgumentException("node with name: \"" + nodeName + "\" not found, because: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public static String getNodeValue(Node rootNode, String nodeName) {
        try {
            if (!rootNode.hasChildNodes()) {
                return null;//???
            }
            Node node = findFirstNode(nodeName, rootNode.getChildNodes());
            if (node == null) {
                return null;
            }
            return node.getTextContent();
        } catch (DOMException ex) {
            throw new IllegalArgumentException("node with name: \"" + nodeName + "\" not found, because: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public static List<Node> getNodeList(String nodeName, Node node) {
//        warning("getNodeList", "Function is under development");
        if (nodeName == null) {
            throw new IllegalArgumentException("nodeName = null");
        }
        if (nodeName.isEmpty()) {
            throw new IllegalArgumentException("nodeName is empty");
        }
        if (node == null) {
            throw new IllegalArgumentException("node = null");
        }
        ArrayList<Node> nodes = new ArrayList<>();
        gettingNodeList(nodes, nodeName, node);
        return nodes;
    }
//---------------------------------------------------------------------------

    private static void gettingNodeList(List<Node> nodes, String nodeName, Node node) {
        if (!node.hasChildNodes()) {
            return;
        }
        NodeList nodeList = node.getChildNodes();
        for (int q = 0; q < nodeList.getLength(); q++) {
            Node subNode = nodeList.item(q);
            String name = subNode.getLocalName();
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (name != null) {
                if (name.equals(nodeName)) {
                    nodes.add(subNode);
                }
            }
            if (subNode.hasChildNodes()) {
                gettingNodeList(nodes, nodeName, subNode);
            }
        }
    }
//---------------------------------------------------------------------------

    public static List<String> getNodeValueList(Document doc, String nodeName)  {
//        warning("getNodeValue", "Function is under development");
            NodeList nodeList = doc.getChildNodes();
            Node node = findNodeList(nodeName, nodeList);
            if (node == null) {
                return null;
            }
            return null;//node.getTextContent();

    }
//---------------------------------------------------------------------------

    /**
     * Получение значения атрибута с именем AttributeName в узле node.<br/>
     *
     * @param node узел, в котором ищится атрибут
     * @param AttributeName имя атрибута
     * @param required обязательность наличия атрибута
     * @return Значение или null если атрибут ненайден и не обязателен
     * @throws AException в случае если атрибут не найден но обязательно должен
     * быть
     */
    public static String getAttribute(Node node, String AttributeName, boolean required) {
        if (node == null) {
            throw new IllegalArgumentException("node = null");
        }
        if (AttributeName == null) {
            throw new IllegalArgumentException("AttributeName = null");
        }
        for (int q = 0; q < node.getAttributes().getLength(); q++) {
            Node attribute = node.getAttributes().item(q);
            if (attribute == null) {
                continue;
            }
            if (!attribute.getLocalName().equals(AttributeName)) {
                continue;
            }
            return attribute.getNodeValue();
        }
        if (required) {
            throw new IllegalArgumentException("Attribute with name \"" + AttributeName + "\" not found");
        }
        return null;
    }
//---------------------------------------------------------------------------

    /**
     * Установка значения value для атрибута с именем AttributeName в узле
     * node.<br/>
     *
     * @param node узел, в котором ищится атрибут
     * @param AttributeName имя атрибута
     * @param value устанавливаемое значение
     * @param required обязательность наличия атрибута
     * @throws AException в случае если атрибут не найден но обязательно должен
     * быть
     */
    public static void setAttribute(Node node, String AttributeName, String value, boolean required) {
        if (node == null) {
            throw new IllegalArgumentException("node = null");
        }
        if (AttributeName == null) {
            throw new IllegalArgumentException("AttributeName = null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value = null");
        }
        for (int q = 0; q < node.getAttributes().getLength(); q++) {
            Node attribute = node.getAttributes().item(q);
            if (attribute == null) {
                continue;
            }
            if (!attribute.getLocalName().equals(AttributeName)) {
                continue;
            }
            attribute.setNodeValue(value);
            return;
        }
        if (required) {
            throw new IllegalArgumentException("Attribute with name \"" + AttributeName + "\" not found");
        }
    }
//---------------------------------------------------------------------------

    public static String printFormat(String xml, int indent) throws TransformerException {
        if (xml == null) {
            throw new IllegalArgumentException("xml = null");
        }
        Source xmlInput = new StreamSource(new StringReader(xml));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", indent);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }
//---------------------------------------------------------------------------

    public static Element appendNodeNS(Node target, Namespace ns, String name) {
        return appendNodeNS(target, ns, name, null);
    }
//---------------------------------------------------------------------------

    public static Element appendNodeNS(Node target, Namespace ns, String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        int index = name.indexOf(":");
        if (index == 0) {
            throw new IllegalArgumentException("prefix is empty");
        }
        String uri = "";
        if (index > 0) {
            String prefix = name.substring(0, index);
            uri = ns.getNamespaceURI(prefix);
        }
        return appendNodeNS(target, uri, name, value);
    }
//---------------------------------------------------------------------------

    public static Element appendNodeNS(Node target, String uri, String name, String value) {
        if (target == null) {
            throw new IllegalArgumentException("target = null");
        }
        if (uri == null) {
            throw new IllegalArgumentException("uri = null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        try {
            Document owner = target.getOwnerDocument();
            Element element = owner.createElementNS(uri, name);
            if (value != null) {
                element.setTextContent(value);
            }
            target.appendChild(element);
            return element;
        } catch (DOMException ex) {
            throw new IllegalArgumentException("append node " + uri + ":" + name + " error: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public static Attr appendAttrNS(Element target, Namespace ns, String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        int index = name.indexOf(":");
        if (index == 0) {
            throw new IllegalArgumentException("prefix is empty");
        }
        String uri = "";
        if (index > 0) {
            String prefix = name.substring(0, index);
            uri = ns.getNamespaceURI(prefix);
        }
        return appendAttrNS(target, uri, name, value);
    }
//---------------------------------------------------------------------------

    public static Attr appendAttrNS(Element target, String uri, String name, String value) {
        if (target == null) {
            throw new IllegalArgumentException("target = null");
        }
        if (uri == null) {
            throw new IllegalArgumentException("uri = null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        try {
            Document owner = target.getOwnerDocument();
            Attr attr = owner.createAttributeNS(uri, name);
            if (value != null) {
                attr.setTextContent(value);
            }
//   target.appendChild(attr);
//   return attr;
            return target.setAttributeNodeNS(attr);
        } catch (DOMException ex) {
            throw new IllegalArgumentException("append attr " + uri + ":" + name + " error: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public static Element appendNodeNS(Node target, String uri, String name) {
        return appendNodeNS(target, uri, name, null);
    }
//---------------------------------------------------------------------------

    public static void importNode(Document targetDoc, Node targetNode, Node imported) throws DOMException {
        targetNode.appendChild(targetDoc.importNode(imported, true));
    }
//---------------------------------------------------------------------------

    public static void importNode(Element target, Node imported) throws DOMException {
        target.appendChild(target.getOwnerDocument().importNode(imported, true));
    }
//---------------------------------------------------------------------------
}
