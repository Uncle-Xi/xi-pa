package com.xipa.parsing;

//import com.sun.corba.se.spi.orb.PropertyParser;

import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @description: XNode
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XNode {

    private static final Logger log = Logger.getLogger(XNode.class.toString());

    private final Node node;
    private final String name;
    private final String body;
    private final Properties attributes;
    private final Properties variables;
    private final XPathParser xpathParser;

    public XNode(XPathParser xpathParser, Node node, Properties variables) {
        this.xpathParser = xpathParser;
        this.node = node;
        this.name = node.getNodeName();
        this.variables = variables;
        this.attributes = parseAttributes(node);
        this.body = parseBody(node);
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    public String getStringBody(String def) {
        if (body == null) {
            return def;
        } else {
            return body;
        }
    }

    public XNode newXNode(Node node) {
        return new XNode(xpathParser, node, variables);
    }

    public List<XNode> evalNodes(String expression) {
        return xpathParser.evalNodes(node, expression);
    }

    public XNode evalNode(String expression) {
        return xpathParser.evalNode(node, expression);
    }

    public Properties getChildrenAsProperties() {
        Properties properties = new Properties();
        for (XNode child : getChildren()) {
            String name = child.getStringAttribute("name");
            String value = child.getStringAttribute("value");
            if (name != null && value != null) {
                properties.setProperty(name, value);
            }
        }
        return properties;
    }

    public String getStringAttribute(String name) {
        return getStringAttribute(name, null);
    }

    public String getStringAttribute(String name, String def) {
        String value = attributes.getProperty(name);
        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    public List<XNode> getChildren() {
        List<XNode> children = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        if (nodeList != null) {
            for (int i = 0, n = nodeList.getLength(); i < n; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    children.add(new XNode(xpathParser, node, variables));
                }
            }
        }
        return children;
    }

    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
//                System.out.println("attribute >>> " + attribute);
//                String value = PropertyParser.parse(attribute.getNodeValue(), variables);
//                System.out.println("attribute value >>> " + value);
                attributes.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }
        return attributes;
    }

    private String parseBody(Node node) {
        String data = getBodyData(node);
        if (data == null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                data = getBodyData(child);
                if (data != null) {
                    break;
                }
            }
        }
        return data;
    }

    private String getBodyData(Node child) {
        if (child.getNodeType() == Node.CDATA_SECTION_NODE
                || child.getNodeType() == Node.TEXT_NODE) {
            String data = ((CharacterData) child).getData();
            data = PropertyParser.parse(data, variables);
            return data;
        }
        return null;
    }
}
