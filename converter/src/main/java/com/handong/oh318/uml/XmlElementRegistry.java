package com.handong.oh318.uml;

import com.handong.oh318.model.ClassType;

public interface XmlElementRegistry {
    BoxStyle getStyle(ClassType type);

    BoxStyle getStyle(ClassType type, BoxStyle style);

    void setStyle(ClassType type, BoxStyle style);
}
