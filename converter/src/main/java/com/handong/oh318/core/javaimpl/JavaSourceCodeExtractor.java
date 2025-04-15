package com.handong.oh318.core.javaimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// TODO jrt parsing .class files and decompile
public class JavaSourceCodeExtractor extends AbsJavaExtractor {

    public JavaSourceCodeExtractor(String extractFromPath) {
        super(extractFromPath);
    }

    /**
     * Get .java files from a directoryPath recursively
     * 
     * @param directoryPath
     *                      directoryPath received by user
     * @return List<Path>
     *
     * @throws IOException
     */
    @Override
    protected List<String> getRawData(String directoryPath) {
        var dirPath = Paths.get(directoryPath);

        if (!Files.exists(dirPath)) {
            throw new RuntimeException("Path must be a directory");
        }

        try {
            return Files.walk(dirPath)
                    .filter(it -> Files.isRegularFile(it) && it.toString()
                            .endsWith(".java"))
                    .map(it -> {
                        try {
                            return Files.readString(it);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}