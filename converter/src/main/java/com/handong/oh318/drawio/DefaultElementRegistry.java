package com.handong.oh318.drawio;

import static com.handong.oh318.drawio.Utils.addAttr;
import static com.handong.oh318.drawio.Utils.addLinemxGeometry;
import static com.handong.oh318.drawio.Utils.addmxGeometry;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handong.oh318.drawio.model.RelationLine;
import com.handong.oh318.drawio.model.box.ClassBox;
import com.handong.oh318.drawio.model.box.ClassBoxSeparator;
import com.handong.oh318.drawio.model.box.ClassBoxText;
import com.handong.oh318.model.ClassType;
import com.handong.oh318.model.ClassUml;

public class DefaultElementRegistry implements XmlElementRegistry {

    private static final String interfaceLineStyleConstant = "endArrow=block;dashed=1;endFill=0;endSize=12;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;";
    private static final String extendLineStyleConstant = "endArrow=block;endSize=16;endFill=0;html=1";

    private Map<ClassType, BoxStyle> STYLE_MAP = Map.of(
            ClassType.CLASS, BoxStyle.getDefault().fontStyle(1).build(),
            ClassType.INTERFACE, BoxStyle.getDefault().bgColor("#d5e8d4").borderColor("#82b366").fontStyle(3).build(),
            ClassType.ABSTRACT_CLASS,
            BoxStyle.getDefault().bgColor("#dae8fc").borderColor("#6c8ebf").fontStyle(3).build(),
            ClassType.ENUM, BoxStyle.getDefault().bgColor("yellow").fontStyle(1).build());

    private Map<String, ElementEntry> elements = new HashMap<>();

    private Document document;
    private Element root;
    private String nodeId;

    private int index;

    public DefaultElementRegistry(Element root, Document document, String nodeId) {
        this.document = document;
        this.root = root;
        this.nodeId = nodeId;
    }

    public DefaultElementRegistry(Element root, Document document, String nodeId,
            Map<ClassType, BoxStyle> classBoxStyles) {
        this(root, document, nodeId);
        this.STYLE_MAP = classBoxStyles;
    }

    public BoxStyle getStyle(ClassType type) {
        return STYLE_MAP.get(type);
    }

    public BoxStyle getStyle(ClassType type, BoxStyle style) {
        return STYLE_MAP.getOrDefault(type, style);
    }

    public void setStyle(ClassType type, BoxStyle style) {
        STYLE_MAP.put(type, style);
    }

    @Override
    public ElementEntry createClassBox(ClassUml classUml, int x, int y) {
        var style = STYLE_MAP.get(classUml.getType());
        var classBoxId = inc();

        var classBox = new ClassBox(classBoxId, classUml, style, 20);
        classBox.setX(x);
        classBox.setY(y);

        // Draw a ClassNameBox
        var classNameBox = document.createElement("mxCell");
        addAttr(classNameBox, "id", classBox.getUmlId());
        addAttr(classNameBox, "value", classBox.getHeaderName());
        addAttr(classNameBox, "style", classBox.getSwimLineAttr().getAttributesString());
        addAttr(classNameBox, "vertex", "1");
        addAttr(classNameBox, "parent", this.nodeId);

        root.appendChild(classNameBox);

        var entry = new ElementEntry(classNameBox, classBox);
        elements.put(classBox.getUmlId(), entry);

        var headerSize = classBox.getSwimLineAttr().getAttribute("startSize");

        if (headerSize == null) {
            headerSize = "30";
            classBox.getSwimLineAttr().addAttribute("startSize", headerSize);
        }

        var inBoxY = Integer.parseInt(headerSize);
        var fields = classBox.getFields();
        for (var field : fields) {
            field.setY(inBoxY);
            registerBoxData(field, classNameBox);
            inBoxY += field.getHeight();
        }

        var separator = registerClassBoxSeparator(classBoxId, classBox, inBoxY, 8, style.getBorderColor());
        inBoxY += separator.getHeight();

        var methods = classBox.getMethods();
        for (var method : methods) {
            method.setY(inBoxY);
            registerBoxData(method, classNameBox);
            inBoxY += method.getHeight();
        }

        classBox.setHeight(inBoxY);
        addmxGeometry(document, classNameBox, classBox);
        return entry;
    }

    private ClassBoxSeparator registerClassBoxSeparator(String parentId, UmlElement umlElement, int y, int height,
            String color) {
        var classBoxSeparator = new ClassBoxSeparator(umlElement);
        classBoxSeparator.setUmlId(inc());
        classBoxSeparator.setHeight(height);
        classBoxSeparator.setY(y);
        classBoxSeparator.setX(-1);
        classBoxSeparator.setWidth(umlElement.getWidth());

        classBoxSeparator.getClassNameAttr().addAttribute("strokeColor", color);

        var separator = document.createElement("mxCell");
        addAttr(separator, "id", classBoxSeparator.getUmlId());
        addAttr(separator, "style", classBoxSeparator.getClassNameAttr().getAttributesString());
        addAttr(separator, "vertex", "1");
        addAttr(separator, "parent", parentId);

        elements.put(classBoxSeparator.getUmlId(), new ElementEntry(separator, classBoxSeparator));
        root.appendChild(separator);

        addmxGeometry(document, separator, classBoxSeparator);
        return classBoxSeparator;
    }

    private Element registerBoxData(ClassBoxText classBoxText, Element parent) {
        var field = document.createElement("mxCell");
        addAttr(field, "id", inc());
        addAttr(field, "value", classBoxText.getText());
        addAttr(field, "style", classBoxText.getInClassAttr().getAttributesString());
        addAttr(field, "vertex", "1");
        addAttr(field, "parent", parent.getAttribute("id"));
        elements.put(classBoxText.getUmlId(), new ElementEntry(field, classBoxText));

        addmxGeometry(document, field, classBoxText);
        root.appendChild(field);
        return field;
    }

    public Element registerLine(int value) {
        Element lines = document.createElement("mxCell");
        addAttr(lines, "id", inc());

        if (value == 0) {
            addAttr(lines, "value", "Extends");
            addAttr(lines, "style", extendLineStyleConstant);
        } else {
            addAttr(lines, "value", "");
            addAttr(lines, "style", interfaceLineStyleConstant);
        }

        addAttr(lines, "edge", "1");
        return lines;
    }

    private String inc() {
        return String.valueOf(++index);
    }

    @Override
    public ElementEntry createLine(int value, String sourceId, String targetId) {
        var line = new RelationLine(value, sourceId, targetId);
        line.setUmlId(inc());

        var lines = document.createElement("mxCell");
        addAttr(lines, "id", line.getUmlId());

        addAttr(lines, "value", line.getValue());
        addAttr(lines, "style", line.getLineAttr().getAttributesString());

        addAttr(lines, "edge", "1");
        addAttr(lines, "parent", this.nodeId);

        addAttr(lines, "source", sourceId);
        addAttr(lines, "target", targetId);

        addLinemxGeometry(document, lines);
        root.appendChild(lines);
        return new ElementEntry(lines, line);
    }
}
