package com.handong.oh318.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ClassId {
    private String name;
    private String packageName;

    @Override
    public String toString() {
        if (packageName == null || packageName.isBlank()) {
            return name;
        } else {
            return packageName + "." + name;
        }
    }
}
