package com.lib.xml;

import org.w3c.dom.Document;

public class XmlReader {
    private Document xmlDoc;

    public XmlReader(String xmlFilePath) throws Exception {
        this(xmlFilePath, null);
    }

    public XmlReader(String xmlFilePath, String encoding) throws Exception {
        xmlDoc = XmlHelpers.TryGetXmlDocument(xmlFilePath, encoding);

        if(xmlDoc == null)
            throw new Exception("Couldn't fetch the XML document...");
    }


}
