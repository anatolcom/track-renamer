/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author anatol
 */
public class Namespace implements NamespaceContext {
//---------------------------------------------------------------------------

    private final Map<String, String> urisByPrefix = new HashMap<>();
    private final Map<String, Set> prefixesByURI = new HashMap<>();
//---------------------------------------------------------------------------

    public Namespace() {
        addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        addNamespace(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    }
//---------------------------------------------------------------------------

    public Namespace(Namespace namespace) {
        addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        addNamespace(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
        urisByPrefix.clear();
        urisByPrefix.putAll(namespace.urisByPrefix);
        prefixesByURI.clear();
        prefixesByURI.putAll(namespace.prefixesByURI);
    }
//---------------------------------------------------------------------------

    public Namespace(String prefix, String namespaceURI) {
        addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
        addNamespace(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
        addNamespace(prefix, namespaceURI);
    }
//---------------------------------------------------------------------------

    public synchronized final void addNamespace(String prefix, String namespaceURI) {
        urisByPrefix.put(prefix, namespaceURI);
        if (prefixesByURI.containsKey(namespaceURI)) {
            prefixesByURI.get(namespaceURI).add(prefix);
        } else {
            Set<String> set = new HashSet<String>();
            set.add(prefix);
            prefixesByURI.put(namespaceURI, set);
        }
    }
//---------------------------------------------------------------------------

    /**
     * добавляет Namespace.<br />
     *
     * @param xmlns пример значения:
     * xmlns:rev="http://smev.gosuslugi.ru/rev111111"
     * @throws AException в случае ошибки
     */
    public synchronized final void addNamespace(String xmlns) throws IllegalArgumentException {
        xmlns = xmlns.trim();
        if (!xmlns.startsWith("xmlns:")) {
            throw new IllegalArgumentException("\"" + xmlns + "\"is not xmlns");
        }
        xmlns = xmlns.substring(6);
        int pos = xmlns.indexOf("=");
        String prefix = xmlns.substring(0, pos);
        String namespaceURI = xmlns.substring(pos + 2, xmlns.length() - 1);
        addNamespace(prefix, namespaceURI);
    }
//---------------------------------------------------------------------------

    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix = null");
        }
        if (!urisByPrefix.containsKey(prefix)) {
            return XMLConstants.NULL_NS_URI;
        }
        return (String) urisByPrefix.get(prefix);
    }
//---------------------------------------------------------------------------

    @Override
    public String getPrefix(String namespaceURI) {
        return (String) getPrefixes(namespaceURI).next();
    }
//---------------------------------------------------------------------------

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI = null");
        }
        if (!prefixesByURI.containsKey(namespaceURI)) {
            return Collections.EMPTY_SET.iterator();
        }
        return prefixesByURI.get(namespaceURI).iterator();
    }
//---------------------------------------------------------------------------
}
