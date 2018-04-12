package com.lib.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class XmlReader {
    private Document xmlDoc;

    public XmlReader(String xmlFilePath) throws Exception {
        this(xmlFilePath, null);
    }

    public XmlReader(String xmlFilePath, String encoding) throws Exception {
        xmlDoc = XmlHelpers.TryGetXmlDocument(xmlFilePath, encoding);

        if (xmlDoc == null)
            throw new Exception("Couldn't fetch the XML document...");

        xmlDoc.getDocumentElement().normalize();
    }

    private List<String> getAttributeValues(Node xmlNode, String[] attributeNames) {
        List<String> values = new ArrayList<>();

        NamedNodeMap attributes = xmlNode.getAttributes();

        for (String attributeName : attributeNames) {
            Node node = attributes.getNamedItem(attributeName);

            if (node == null)
                continue;

            String value = node.getNodeValue();

            if (value != null)
                values.add(value);
        }

        return values;
    }

    public <T> List<T> getObjects(XmlTag targetTag,
                                  Function<List<String>, T> converter,
                                  XmlTag... childTagsToFind) throws Exception {
        List<T> objects = new ArrayList<>();

        //Find all the tags with the wanted name
        NodeList targetNodes = xmlDoc.getElementsByTagName(targetTag.getTagName());

        //Loop through the found tags
        for (int i = 0; i < targetNodes.getLength(); i++) {
            //Create a list that holds current node's attribute values and its children's node and attribute values
            List<String> itemData = new ArrayList<>();

            //Node that holds all the wanted values (its children are leaf nodes)
            Node targetNode = targetNodes.item(i);

            //If there are attributes that need to be found, try to add them to the list
            if (targetTag.hasAttributes())
                itemData.addAll(getAttributeValues(targetNode, targetTag.getAttributeNames()));

            //Get the child nodes of the current target node
            NodeList children = targetNode.getChildNodes();
            for (XmlTag childTag : childTagsToFind) {

                for (int j = 0; j < children.getLength(); j++) {
                    //Current child node (j loop) of the current target node (i loop)
                    Node childNode = children.item(j);

                    //Name of the tag user wants to find
                    String childTagName = childTag.getTagName();
                    //Name of the current child node
                    String childNodeName = childNode.getNodeName();

                    //If the child node's name matches the one that was requested by the user,
                    //try to add its node value and its attribute values to the collection
                    if (childNodeName.equals(childTagName)) {
                        String nodeValue = childNode.getTextContent();

                        if(nodeValue != null)
                            itemData.add(nodeValue);

                        if (childTag.hasAttributes())
                            itemData.addAll(getAttributeValues(childNode, childTag.getAttributeNames()));
                    }
                }//for(j) - loops through the children of the target tag

            }//foreach - loops through the requested child nodes that are used to create the object

            //Create an instance of the object using the converter and add it to the list
            objects.add(converter.apply(itemData));
        }//for(i) - loops through the target nodes

        return objects;
    }
}
