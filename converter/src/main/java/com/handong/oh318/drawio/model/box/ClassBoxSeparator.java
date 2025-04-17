package com.handong.oh318.drawio.model.box;

import com.handong.oh318.drawio.ElementAttributes;
import com.handong.oh318.drawio.UmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassBoxSeparator extends UmlElement {

    public ClassBoxSeparator(UmlElement copy) {
        super(copy);
    }

    private final ElementAttributes classNameAttr = new ElementAttributes(
            "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;");
}
