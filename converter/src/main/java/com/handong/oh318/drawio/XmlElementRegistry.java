package com.handong.oh318.drawio;

import com.handong.oh318.model.ClassType;
import com.handong.oh318.model.ClassUml;

public interface XmlElementRegistry {
    BoxStyle getStyle(ClassType type);

    BoxStyle getStyle(ClassType type, BoxStyle style);

    void setStyle(ClassType type, BoxStyle style);

    ElementEntry createClassBox(ClassUml classUml, int x, int y);

    ElementEntry createLine(int value, String sourceId, String targetId);
}
