package com.handong.oh318.uml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class ElementAttributes {
    private String name;
    private Map<String, String> attributes;

    // attrs is "name;key=value;..." string
    public ElementAttributes(String attrs) {
        this.name = attrs.substring(0, attrs.indexOf(";"));
        this.attributes = Arrays.stream(attrs.substring(attrs.indexOf(";") + 1).split(";"))
                .map(attr -> attr.split("="))
                .collect(Collectors.toMap(
                        attr -> attr[0],
                        attr -> attr[1]));

    }

    public ElementAttributes() {
        this.name = "";
        this.attributes = new HashMap<>();
    }

    public String getAttributesString() {
        return name + ";" + this.attributes.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(";"));
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return this.attributes.get(key);
    }
}
