package com.handong.oh318.uml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handong.oh318.model.ClassType;
import com.handong.oh318.model.ClassUml;
import com.handong.oh318.uml.box.ClassBox;

public class DefaultElementRegistry implements XmlElementRegistry {
    public static final Map<ClassType, BoxStyle> STYLE_MAP = Map.of(
            ClassType.CLASS, BoxStyle.getDefault().fontStyle(1).build(),
            ClassType.INTERFACE, BoxStyle.getDefault().bgColor("green").fontStyle(3).build(),
            ClassType.ABSTRACT_CLASS, BoxStyle.getDefault().bgColor("blue").fontStyle(3).build(),
            ClassType.ENUM, BoxStyle.getDefault().bgColor("yellow").fontStyle(1).build());

    private Document document;
    private Map<Integer, ElementEntry> elements = new HashMap<>();

    public record ElementEntry(Element element, ClassBox classBox) {
    }

    private int index;

    public BoxStyle getStyle(ClassType type) {
        return STYLE_MAP.get(type);
    }

    public BoxStyle getStyle(ClassType type, BoxStyle style) {
        return STYLE_MAP.getOrDefault(type, style);
    }

    public void setStyle(ClassType type, BoxStyle style) {
        STYLE_MAP.put(type, style);
    }

    public Element registerClassBox(ClassUml classUml) {
        var style = STYLE_MAP.get(classUml.getType());
        var classBox = new ClassBox(classUml, style);

        // Draw a ClassNameBox
        var classNameBox = document.createElement("mxCell");
        addAttr(classNameBox, "id", index + "");
        addAttr(classNameBox, "value", classUml.getClassId().getName());
        addAttr(classNameBox, "style", classBox.getSwimLineAttr().getAttributesString());
        addAttr(classNameBox, "vertex", "1");
        // addAttr(classNameBox, "parent", "1");

        elements.put(index, new ElementEntry(classNameBox, classBox));

        index++;

        return classNameBox;
    }

    private void addAttr(Element element, String attrName, String attrValue) {
        element.setAttribute(attrName, attrValue);
    }
}
