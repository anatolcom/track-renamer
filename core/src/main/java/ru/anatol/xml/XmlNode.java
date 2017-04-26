/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Anatol
 */
public class XmlNode {

    private final Node node;

    public XmlNode(Node node) {
        this.node = node;
    }
//---------------------------------------------------------------------------

    /**
     * Получение значения атрибута с именем AttributeName в узле node.
     *
     * @param node узел, в котором ищится атрибут
     * @param AttributeName имя атрибута
     * @param required обязательность наличия атрибута
     * @return Значение или null если атрибут ненайден и не обязателен
     * @throws AException в случае если атрибут не найден но обязательно должен
     * быть
     */
    public String getAttribute(String AttributeName, boolean required) {
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
            throw new IllegalArgumentException("attribute with name \"" + AttributeName + "\" not found");
        }
        return null;
    }
//---------------------------------------------------------------------------

    /**
     * Установка значения value для атрибута с именем AttributeName в узле node.
     *
     * @param node узел, в котором ищится атрибут
     * @param AttributeName имя атрибута
     * @param value устанавливаемое значение
     * @param required обязательность наличия атрибута
     * @throws AException в случае если атрибут не найден но обязательно должен
     * быть
     */
    public void setAttribute(String AttributeName, String value, boolean required) {
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
            throw new IllegalArgumentException("attribute with name \"" + AttributeName + "\" not found");
        }
    }
//---------------------------------------------------------------------------

    public Element appendNodeNS(Namespace ns, String name) throws IllegalArgumentException {
        return appendNodeNS(ns, name, null);
    }
//---------------------------------------------------------------------------

    public Element appendNodeNS(Namespace ns, String name, String value) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        int index = name.indexOf(":");
        if (index == 0) {
            throw new IllegalArgumentException("prefix is empty");
        }
        String uri = "";
        if (index > 0) {
            uri = ns.getNamespaceURI(name.substring(0, index));
        }
        return appendNodeNS(uri, name, value);
    }
//---------------------------------------------------------------------------

    public Element appendNodeNS(String uri, String name, String value) throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException("uri = null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        try {
            Element element = node.getOwnerDocument().createElementNS(uri, name);
            if (value != null) {
                element.setTextContent(value);
            }
            node.appendChild(element);
            return element;
        } catch (DOMException ex) {
            throw new IllegalArgumentException("append node " + uri + ":" + name + " error: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public Attr appendAttrNS(Namespace ns, String name, String value) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name = null");
        }
        int index = name.indexOf(":");
        if (index == 0) {
            throw new IllegalArgumentException("prefix is empty");
        }
        String uri = "";
        if (index > 0) {
            uri = ns.getNamespaceURI(name.substring(0, index));
        }
        return appendAttrNS(uri, name, value);
    }
//---------------------------------------------------------------------------

    public Attr appendAttrNS(String uri, String name, String value) throws IllegalArgumentException {
        if (!(node instanceof Element)) {
            throw new IllegalArgumentException("node not is Element");
        }
        Element target = (Element) node;
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
            Attr attr = target.getOwnerDocument().createAttributeNS(uri, name);
            if (value != null) {
                attr.setTextContent(value);
            }
            return target.setAttributeNodeNS(attr);
        } catch (DOMException ex) {
            throw new IllegalArgumentException("append attr " + uri + ":" + name + " error: " + ex.getMessage(), ex);
        }
    }
//---------------------------------------------------------------------------

    public Element appendNodeNS(String uri, String name) throws IllegalArgumentException {
        return appendNodeNS(uri, name, null);
    }
//---------------------------------------------------------------------------

}
