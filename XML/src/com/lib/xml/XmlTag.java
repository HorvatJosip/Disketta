package com.lib.xml;

public class XmlTag {
    private String tagName;
    private String[] attributeNames;

    public XmlTag(String tagName){
        this.tagName = tagName;
    }

    public XmlTag(String tagName, String... attributeNames) {
        this(tagName);
        this.attributeNames = attributeNames;
    }

    public String getTagName() {
        return tagName;
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }

    public boolean hasAttributes(){
        return attributeNames != null && attributeNames.length > 0;
    }
}
