package com.handong.oh318.uml;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.forge.roaster.model.Visibility;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handong.oh318.UserInput;
import com.handong.oh318.core.SourceCodeExtractor;
import com.handong.oh318.model.ClassUml;
import com.handong.oh318.uml.box.ClassBox;

public class UmlDrawEngine extends UserInput {
    private static final String interfaceLineStyleConstant = "endArrow=block;dashed=1;endFill=0;endSize=12;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;";
    private static final String extendLineStyleConstant = "endArrow=block;endSize=16;endFill=0;html=1";

    private String drawioPath; // drawio path that .drawio file will be created
    private String directoryPath; // Source classes directory path by user input

    private List<ClassUml> classBoxes;
    private XmlElementRegistry boxStyleRegistry;
    private Set<String> visited;

    private Document document;
    private Element root;

    private int maxHeight;
    private int maxWidth;

    public UmlDrawEngine(SourceCodeExtractor sourceCodeExtractor, XmlElementRegistry boxStyleRegistry) {
        this.classBoxes = sourceCodeExtractor.extractSource();
        this.boxStyleRegistry = boxStyleRegistry;
    }

    public UmlDrawEngine(SourceCodeExtractor sourceCodeExtractor) {
        this(sourceCodeExtractor, new DefaultElementRegistry());
    }

    /**
     * Main for draw a .drawio XML file
     */
    public void createDrawio() {
        // create the xml file
        initXMLfile();
        // transform the DOM Object to an XML File
        createFile();
        System.out.println("Done creating XML File");
    }

    /**
     * create the xml file
     * Most parts are consist of constant value
     * You can fix or update this part by yourself.
     */
    public void initXMLfile() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();

            Element diagram = document.createElement("diagram");
            addAttr(diagram, "id", UUID.randomUUID().toString());
            addAttr(diagram, "name", "Page-1"); //
            document.appendChild(diagram);

            // Employee element (Constant)
            Element mxGraphModel = document.createElement("mxGraphModel");
            addAttr(mxGraphModel, "dx", "332");
            addAttr(mxGraphModel, "dy", "241");
            addAttr(mxGraphModel, "grid", "1");
            addAttr(mxGraphModel, "gridSize", "10");
            addAttr(mxGraphModel, "guides", "1");
            addAttr(mxGraphModel, "tooltips", "1");
            addAttr(mxGraphModel, "connect", "1");
            addAttr(mxGraphModel, "arrows", "1");
            addAttr(mxGraphModel, "fold", "1");
            addAttr(mxGraphModel, "page", "1");
            addAttr(mxGraphModel, "pageScale", "1");
            addAttr(mxGraphModel, "pageWidth", "850");
            addAttr(mxGraphModel, "pageHeight", "1100");
            addAttr(mxGraphModel, "math", "0");
            addAttr(mxGraphModel, "shadow", "0");

            diagram.appendChild(mxGraphModel);

            root = document.createElement("root");
            mxGraphModel.appendChild(root);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Create XML file form Classbox that Wrapper class of JavaClassSource object
     * First loop is for Class
     * Second loop is for Line (Extends, Interface, ... will be updated)
     */
    public void createFile() {

        // transform the DOM Object to an XML File
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(drawioPath));
            transformer.transform(domSource, streamResult);
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * helper function for draw
     *
     * @param element
     * 
     * @param attrName
     * 
     * @param attrValue
     */
    public void addAttr(Element element, String attrName, String attrValue) {
        element.setAttribute(attrName, attrValue);
    }

    // helper function for draw2 (Child)
    public void addmxGeometry(Element element, int x, int y, int width, int height) {
        Element mxGeometry = document.createElement("mxGeometry");
        if (x != -1)
            addAttr(mxGeometry, "x", Integer.toString(x));
        if (y != -1)
            addAttr(mxGeometry, "y", Integer.toString(y));
        addAttr(mxGeometry, "width", Integer.toString(width));
        addAttr(mxGeometry, "height", Integer.toString(height));
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    // helper function for draw3 (Lines)
    public void addLinemxGeometry(Element element) {
        Element mxGeometry = document.createElement("mxGeometry");
        addAttr(mxGeometry, "width", "160");
        addAttr(mxGeometry, "relative", "1");
        addAttr(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    /**
     * Drawing a classboxes on XML file
     * 
     * @param classbox
     *                 Wapper class for JavaClassSoure contining class information
     * @param cid
     *                 variable for prevent a duplicated XML
     */
    public void drawClass(ClassBox classbox, int cid) {

        // for XML header
        if (cid == 1) {
            Element biggestBox = document.createElement("mxCell");
            addAttr(biggestBox, "id", Integer.toString(id++));
            root.appendChild(biggestBox);

            Element biggerBox = document.createElement("mxCell");
            addAttr(biggerBox, "id", Integer.toString(id++));
            addAttr(biggerBox, "parent", "0");
            root.appendChild(biggerBox);
        }

        // Draw a ClassNameBox
        Element classNameBox = document.createElement("mxCell");
        addAttr(classNameBox, "id", "");
        addAttr(classNameBox, "value", classbox.getClassName());
        addAttr(classNameBox, "style", classNameStyleConstant);
        addAttr(classNameBox, "vertex", "1");
        addAttr(classNameBox, "parent", "1");
        root.appendChild(classNameBox);

        addmxGeometry(classNameBox, classbox.getX(), classbox.getY(), classbox.getWidth(), classbox.getHeight());

        // TODO Draw a AttributesBox
        // int y = classbox.getFieldboxInfo().getY();
        // List<FieldSource<JavaClassSource>> fieldList =
        // classbox.getJavaClassSource().getFields();
        // for (FieldSource<JavaClassSource> field : fieldList) {
        // drawField(field, classID, y, classbox.getWidth());
        // y += 26;
        // }

        // Draw a SeperatorLine
        // drawSeperatorLine(classID, y, classbox.getWidth());
        // y += 8;

        // TODP Draw a MethodsBox

    }

    // Draw a SeperatorLine
    public void drawSeperatorLine(int classID, int y, int width) {
        Element seperator = document.createElement("mxCell");
        addAttr(seperator, "id", Integer.toString(id++));
        addAttr(seperator, "style", speratorLineStyleConstant);
        addAttr(seperator, "vertex", "1");
        addAttr(seperator, "parent", Integer.toString(classID));
        addmxGeometry(seperator, -1, y, width, 8);
        root.appendChild(seperator);
    }

    /**
     * Drawing a fieldbox
     *
     * @param f
     *                Objects with information for the field
     * @param classID
     *                parent class id of this field
     * @param y
     *                y-axis for field
     * @param width
     *                width for field
     */
    public void drawField(FieldSource<JavaClassSource> f, int classID, int y, int width) {
        Element fieldBox = document.createElement("mxCell");
        addAttr(fieldBox, "id", "");

        String valueString = "";

        String typesArgs = "";
        int typeArgsSize = f.getType().getTypeArguments().size();

        for (int i = 0; i < typeArgsSize; i++) {
            if (i > 0) {
                typesArgs += (f.getType().getTypeArguments().get(i).toString() + ", ");
            } else {
                typesArgs += f.getType().getTypeArguments().get(i).toString();
            }
        }

        if (typeArgsSize > 0) {

            if (f.getVisibility() == Visibility.PUBLIC) {
                valueString = ("+ " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            } else if (f.getVisibility() == Visibility.PROTECTED) {
                valueString = ("# " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            } else if (f.getVisibility() == Visibility.PRIVATE) {
                valueString = ("- " + f.getName() + " : " + f.getType() + "&lt;" + typesArgs + "&gt;");
            }
        } else {
            if (f.getVisibility() == Visibility.PUBLIC) {
                valueString = "+ " + f.getName() + " : " + f.getType() + typesArgs;
            } else if (f.getVisibility() == Visibility.PROTECTED) {
                valueString = "# " + f.getName() + " : " + f.getType() + typesArgs;
            } else if (f.getVisibility() == Visibility.PRIVATE) {
                valueString = "- " + f.getName() + " : " + f.getType() + typesArgs;
            }
        }

        addAttr(fieldBox, "value", valueString);

        if (f.isStatic()) {
            if (!f.isFinal()) {
                addAttr(fieldBox, "style", inclassStyleConstant + "fontStyle=4;");
            } else {
                addAttr(fieldBox, "style", inclassStyleConstant);
            }
        } else {
            addAttr(fieldBox, "style", inclassStyleConstant);
        }

        addAttr(fieldBox, "vertex", "1");
        addAttr(fieldBox, "parent", Integer.toString(classID));
        addmxGeometry(fieldBox, -1, y, width, 26);
        root.appendChild(fieldBox);
    }

    // Draw a MethodsBox
    public void drawMethod(MethodSource<JavaClassSource> m, int classID, int y, int width) {

    }

    /**
     * Draw a Relationship lines
     * 
     * @param value
     *               Variable to distinguish types of relationship line
     * @param target
     *               End classbox of line
     * @param source
     *               Start classbox of line
     */
    public void drawLines(int value, ClassBox target, ClassBox source) {

        Element lines = document.createElement("mxCell");
        addAttr(lines, "id", Integer.toString(id++));

        if (value == 0) {
            addAttr(lines, "value", "Extends");
            addAttr(lines, "style", extendLineStyleConstant);
        } else {
            addAttr(lines, "value", "");
            addAttr(lines, "style", interfaceLineStyleConstant);
        }

        addAttr(lines, "edge", "1");
        addAttr(lines, "parent", "1");

        addAttr(lines, "source", source.getId());
        addAttr(lines, "target", target.getId());

        addLinemxGeometry(lines, target, source);
        root.appendChild(lines);
    }

    public String getDrawioPath() {
        return this.drawioPath;
    }

    public String getDirectoryPath() {
        return this.directoryPath;
    }

    public Document getDocument() {
        return this.document;
    }
}
