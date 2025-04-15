package com.handong.oh318;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.roaster.model.source.JavaSource;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class ExtracterTest {

    Extractor extractor = null;

    @Before
    public void beforeText() {
        extractor = new Extractor("src/test/resource/create", "src/test/resource");
    }

    @Test
    public void testGetJavaFilepaths() {
        try {
            ArrayList<Path> paths = (ArrayList<Path>) extractor.getJavaFilepaths(extractor.getDirectoryPath());

            assertEquals(paths.size(), 7);

            for (int i = 0; i < paths.size(); i++) {
                assertThat(paths.get(i).toString()).endsWith(".java");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Cannot find the files");
        }
    }

    @Test
    public void testGetJavaClassSources() {
        try {
            ArrayList<Path> paths = (ArrayList<Path>) extractor.getJavaFilepaths(extractor.getDirectoryPath());
            List<JavaClassSourceBox> jcs = extractor.getJavaClassSources(".java");

            assertEquals(paths.size(), jcs.size());
        } catch (IOException e) {
            System.err.println("Cannot find the files");
        }
    }

    @Test
    public void testGetJaveClassSource() {
        try {
            JavaSource<?> jcs = extractor.getJaveClassSource("src/test/resource/create/Teslr.java");

            assertEquals(jcs.getName(), "Teslr");
        } catch (IOException e) {
            System.err.println("Cannot find the files");
        }
    }

    @Test
    public void testInitXMLFile() {
        extractor.initXMLfile();
        Document document = extractor.getDocument();

        assertNotNull(document);
    }
}
