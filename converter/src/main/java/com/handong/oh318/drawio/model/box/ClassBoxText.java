package com.handong.oh318.drawio.model.box;

import com.handong.oh318.drawio.ElementAttributes;
import com.handong.oh318.drawio.UmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassBoxText extends UmlElement {
    private String text;
    private final ElementAttributes inClassAttr = new ElementAttributes(
            "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;");
}
