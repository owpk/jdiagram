package com.handong.oh318.drawio.model;

import com.handong.oh318.drawio.ElementAttributes;
import com.handong.oh318.drawio.UmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationLine extends UmlElement {
    private String value;
    private String source;
    private String target;

    private ElementAttributes lineAttr;

    public RelationLine(int type, String source, String target) {
        this.source = source;
        this.target = target;
        this.value = type == 1 ? "extends" : "";
        this.lineAttr = new ElementAttributes(switch (type) {
            case 0 -> "line;endArrow=block;endFill=0;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;";
            case 1 -> "endArrow=block;endSize=16;endFill=0;html=1";
            default -> "endArrow=block;";
        });
    }
}
