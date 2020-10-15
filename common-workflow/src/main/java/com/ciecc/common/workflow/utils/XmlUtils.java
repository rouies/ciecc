package com.ciecc.common.workflow.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.Map;

public class XmlUtils {

    private Document document;

    public XmlUtils(URL path) throws DocumentException {
        SAXReader reader = new SAXReader(new DocumentFactory());
        this.document = reader.read(path);
    }

    public XmlUtils(URL path,Map<String, String> namespances) throws DocumentException {
        SAXReader reader = new SAXReader(new DocumentFactory());
        reader.getDocumentFactory().setXPathNamespaceURIs(namespances);
        this.document = reader.read(path);
    }

    public Document getDocument() {
        return document;
    }
}
