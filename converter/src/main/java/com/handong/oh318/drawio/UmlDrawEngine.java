package com.handong.oh318.drawio;

import static com.handong.oh318.drawio.Utils.addAttr;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.handong.oh318.UserInput;
import com.handong.oh318.core.SourceCodeExtractor;
import com.handong.oh318.model.ClassUml;

public class UmlDrawEngine extends UserInput {

    private String drawioPath; // drawio path that .drawio file will be created

    private List<ClassUml> classBoxes;
    private Map<String, UmlRelations> cache = new HashMap<>();

    private XmlElementRegistry xmlElementRegistry;

    private Document document;
    private Element root;
    private String nodeId;

    private int maxHeight;
    private int maxWidth;

    private static record UmlRelations(String umlId, Set<String> relations) {
    }

    public UmlDrawEngine(SourceCodeExtractor sourceCodeExtractor, String drawioPath) {
        this.classBoxes = sourceCodeExtractor.extractSource();
        this.drawioPath = drawioPath;
        this.document = createXmlDocument();
        this.root = initXMLfile(document);
        this.nodeId = "a_1";
        this.xmlElementRegistry = new DefaultElementRegistry(root, document, nodeId);
    }

    // private Map<ClassBox, List<ClassBox>> getBoxMap(List<ClassUml> classBoxes) {
    // }

    /**
     * Main for draw a .drawio XML file
     * 
     * Create XML file form Classbox that Wrapper class of JavaClassSource object
     * First loop is for Class
     * Second loop is for Line (Extends, Interface, ... will be updated)
     */
    public void createDrawio() {

        var biggestBox = document.createElement("mxCell");
        addAttr(biggestBox, "id", "a_0");
        root.appendChild(biggestBox);

        var biggerBox = document.createElement("mxCell");
        addAttr(biggerBox, "id", nodeId);
        addAttr(biggerBox, "parent", "a_0");
        root.appendChild(biggerBox);

        int x = 100;
        int y = 100;

        iterateOverClasses(classBoxes);

        for (var classBox : classBoxes) {
            var entry = xmlElementRegistry.createClassBox(classBox, x, y);
            x += entry.umlElement().width;
            y += entry.umlElement().height;
        }

        // transform the DOM Object to an XML File
        writeeXMLFile(drawioPath);
    }

    private void iterateOverClasses(List<ClassUml> classUmls) {
        for (var uml : classUmls) {
            iterateOverElement(uml, null);
        }
    }

    private void iterateOverElement(ClassUml classUml, String parentClassRelationId) {

        var classIdentity = classUml.getClassIdentity();

        String umlIdCurrent = null;
        if (!cache.containsKey(classIdentity)) {
            var current = xmlElementRegistry.createClassBox(classUml, 0, 0);
            umlIdCurrent = current.umlElement().umlId;
            cache.put(classIdentity, new UmlRelations(umlIdCurrent, new HashSet<>()));
        } else {
            umlIdCurrent = cache.get(classIdentity).umlId;
        }

        if (parentClassRelationId != null && cache.containsKey(parentClassRelationId)) {
            var relations = cache.get(parentClassRelationId).relations();
            relations.add(umlIdCurrent);
        }

        var parent = classUml.getParent();
        for (var p : parent) {
            iterateOverElement(p, classIdentity);
        }
    }

    private void writeeXMLFile(String drawioPath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(drawioPath));
            transformer.transform(domSource, streamResult);
            System.out.println("Done creating XML File");
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    private Document createXmlDocument() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }
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
    private void drawLines(int value, UmlElement target, UmlElement source) {
        xmlElementRegistry.createLine(value, source.getUmlId(), target.getUmlId());
    }

    /**
     * create the xml file
     * Most parts are consist of constant value
     * You can fix or update this part by yourself.
     */
    private Element initXMLfile(Document document) {
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
        return root;
    }

}
