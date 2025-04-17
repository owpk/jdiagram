package com.handong.oh318.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(of = "classId")
public class ClassUmlElementEntry {
    String classId;
    UmlIdAware umlElement;
}
