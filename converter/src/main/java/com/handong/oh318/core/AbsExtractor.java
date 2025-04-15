package com.handong.oh318.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.handong.oh318.model.ClassUml;

public abstract class AbsExtractor implements SourceCodeExtractor {
    protected final Map<String, ClassUml> sourceEntries = new HashMap<>();
    protected final Queue<ChainEntry> unresolwedChains = new LinkedList<>();

    private String extractPath;

    public static record ChainEntry(String parentType, ClassUml uml) {
    }

    public AbsExtractor(String extractFromPath) {
        this.extractPath = extractFromPath;
    }

    @Override
    public List<ClassUml> extractSource() {
        var rawSources = getRawData(extractPath);
        var sources = rawSources.stream().map(it -> getUmlSource(it)).toList();

        while (!unresolwedChains.isEmpty()) {
            var next = unresolwedChains.poll();
            var parent = sourceEntries.get(next.parentType());
            if (parent != null) {
                next.uml.getParent().add(parent);
            } else {
                System.out.println("Unresolved parent type: " + next.parentType());
            }
        }
        return sources;
    }

    protected abstract List<String> getRawData(String rawClassData);

    protected abstract ClassUml getUmlSource(String path) throws RuntimeException;

}
