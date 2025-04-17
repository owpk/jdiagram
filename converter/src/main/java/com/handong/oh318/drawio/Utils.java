package com.handong.oh318.drawio;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Utils {

    public static void addmxGeometry(Document document, Element element, int x, int y, int width, int height) {
        var mxGeometry = document.createElement("mxGeometry");
        if (x != -1)
            addAttr(mxGeometry, "x", Integer.toString(x));
        if (y != -1)
            addAttr(mxGeometry, "y", Integer.toString(y));
        addAttr(mxGeometry, "width", Integer.toString(width));
        addAttr(mxGeometry, "height", Integer.toString(height));
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    public static void addmxGeometry(Document document, Element element, UmlElement umlElement) {
        var mxGeometry = document.createElement("mxGeometry");
        if (umlElement.x != -1)
            addAttr(mxGeometry, "x", Integer.toString(umlElement.x));
        if (umlElement.y != -1)
            addAttr(mxGeometry, "y", Integer.toString(umlElement.y));
        addAttr(mxGeometry, "width", Integer.toString(umlElement.width));
        addAttr(mxGeometry, "height", Integer.toString(umlElement.height));
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    public static void addAttr(Element element, String attrName, String attrValue) {
        element.setAttribute(attrName, attrValue);
    }

    public static void addLinemxGeometry(Document document, Element element) {
        Element mxGeometry = document.createElement("mxGeometry");
        addAttr(mxGeometry, "width", "160");
        addAttr(mxGeometry, "relative", "1");
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }
}
