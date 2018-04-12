package com.lib.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public final class XmlHelpers {
    private XmlHelpers() { }

    public static Document TryGetXmlDocument(String xmlFilePath){
        return TryGetXmlDocument(xmlFilePath, null);
    }

    public static Document TryGetXmlDocument(String xmlFilePath, String encoding){
        try {
            InputStream inputStream = new FileInputStream(xmlFilePath);
            Reader reader;
            InputSource inputSource;

            if(encoding != null) {
                reader = new InputStreamReader(inputStream, encoding);
                inputSource = new InputSource(reader);
                inputSource.setEncoding(encoding);
            }
            else{
                reader = new InputStreamReader(inputStream);
                inputSource = new InputSource(reader);
            }

            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputSource);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }
}
