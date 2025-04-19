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
            case 1 -> "endArrow=block;endSize=16;endFill=0;html=1";
            case 0 -> "endArrow=block;";
            default -> "";
        });
        this.lineAttr.addAttribute("edgeStyle", "orthogonalEdgeStyle");
        this.lineAttr.addAttribute("jumpStyle", "arc");
    }
}
