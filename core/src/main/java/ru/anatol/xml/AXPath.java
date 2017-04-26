/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author anatol
 */
public class AXPath {
//---------------------------------------------------------------------------

    public static Object evaluateExpression(String expression, Node source, NamespaceContext nsContexts, QName returnType) {
        if (expression == null) {
            throw new IllegalArgumentException("expression = null");
        }
        if (source == null) {
            throw new IllegalArgumentException("source = null");
        }
        if (returnType == null) {
            throw new IllegalArgumentException("returnType = null");
        }
        try {
//   expression = expression.replace("@", "/attribute::");
            XPath xpath = XPathFactory.newInstance().newXPath();
            if (nsContexts != null) {
                xpath.setNamespaceContext(nsContexts);
            }
            XPathExpression exp = xpath.compile(expression);
            return exp.evaluate(source, returnType);
        } catch (XPathExpressionException ex) {
            if (ex.getCause() == null) {
                throw new IllegalArgumentException("expression \"" + expression + "\" " + ex.getMessage(), ex);
            } else {
                throw new IllegalArgumentException("expression \"" + expression + "\" " + ex.getCause().getMessage(), ex);
            }
        }
    }
//---------------------------------------------------------------------------

    /**
     * Выборка одного узла в соответствии с XPath выражением expression
     * относительно узла source.
     *
     * @param source узел, относительно которого производится поиск.
     * @param expression XPath выражение.
     * @param nsContexts нэймспэйс. может быть null.
     * @param required true - узел обязательно должен быть, иначе исключение.
     * false - узла может не быть, вернётся null.
     * @return найденный узел. Если required - false, то может вернутся null.
     */
    public static Node getNode(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        Node node = (Node) evaluateExpression(expression, source, nsContexts, XPathConstants.NODE);
        if (node == null && required) {
            throw new IllegalArgumentException("Node with expression \"" + expression + "\" not found");
        }
        return node;
    }
//---------------------------------------------------------------------------

    /**
     * Выборка одного узла в соответствии с XPath выражением expression
     * относительно узла source.
     *
     * @param source узел, относительно которого производится поиск.
     * @param expression XPath выражение.
     * @param required true - узел обязательно должен быть, иначе исключение.
     * false - узла может не быть, вернётся null.
     * @return найденный узел. Если required - false, то может вернутся null.
     */
    public static Node getNode(Node source, String expression, boolean required) {
        return getNode(source, expression, null, required);
    }
//---------------------------------------------------------------------------

    /**
     * Выборка нескольких узлов в соответствии с XPath выражением expression
     * относительно узла source.
     *
     * @param source узел, относительно которого производится поиск.
     * @param expression XPath выражение.
     * @param nsContexts нэймспэйс. может быть null.
     * @param required true - обязательно хотябы 1 узел в списке, иначе
     * исключение. false - узлов может не быть вовсе, вернётся пустой список.
     * @return список найденных узлов. Никогда не возвращает null, даже при
     * required - false.
     */
    public static NodeList getNodeList(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        NodeList nodeList = (NodeList) evaluateExpression(expression, source, nsContexts, XPathConstants.NODESET);
        if (nodeList.getLength() == 0 && required) {
            throw new IllegalArgumentException("NodeList with expression \"" + expression + "\" not found");
        }
        return nodeList;
    }
//---------------------------------------------------------------------------

    public static String getValue(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        Node node = getNode(source, expression, nsContexts, required);
        if (node == null) {
            return null;
        }
        return node.getTextContent();
    }
//---------------------------------------------------------------------------

    public static String getValueAsString(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        return getValue(source, expression, nsContexts, required);
    }
//---------------------------------------------------------------------------

    public static Integer getValueAsInteger(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        String value = getValue(source, expression, nsContexts, required);
        try {
            if (value == null) {
                return null;
            }
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("value \"" + value + "\" is not a Integer", ex);
        }
    }
//---------------------------------------------------------------------------

    public static Long getValueAsLong(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        String value = getValue(source, expression, nsContexts, required);
        try {
            if (value == null) {
                return null;
            }
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("value \"" + value + "\" is not a Long", ex);
        }
    }
//---------------------------------------------------------------------------

    public static Double getValueAsDouble(Node source, String expression, NamespaceContext nsContexts, boolean required) {
        String value = getValue(source, expression, nsContexts, required);
        try {
            if (value == null) {
                return null;
            }
            return Double.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("value \"" + value + "\" is not a Double", ex);
        }
    }
//---------------------------------------------------------------------------

    public static Date getValueAsDate(Node source, String expression, NamespaceContext nsContexts, boolean required, DateFormat dateFormat) {
        String value = getValue(source, expression, nsContexts, required);
        try {
            if (value == null) {
                return null;
            }
            return dateFormat.parse(value);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("value \"" + value + "\" is not a Date", ex);
        }
    }
//---------------------------------------------------------------------------

    public static void setValue(Node source, String expression, String value, NamespaceContext nsContexts, boolean required) {
        Node node = getNode(source, expression, nsContexts, required);
        if (node != null) {
            node.setTextContent(value);
        }
    }
//---------------------------------------------------------------------------

    public static void setValue(Node source, String expression, String value, boolean required) {
        setValue(source, expression, value, null, required);
    }
//---------------------------------------------------------------------------

    public static Document setValues(Document doc, NamespaceContext nsContexts, Map<String, String> map, boolean required) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            setValue(doc, entry.getKey(), entry.getValue(), nsContexts, required);
        }
        return doc;
    }
//---------------------------------------------------------------------------

    public static Node setValues(Node source, NamespaceContext nsContexts, Map<String, String> map, boolean required) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            setValue(source, entry.getKey(), entry.getValue(), nsContexts, required);
        }
        return source;
    }
//---------------------------------------------------------------------------

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
}
