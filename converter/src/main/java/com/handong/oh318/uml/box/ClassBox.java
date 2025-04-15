package com.handong.oh318.uml.box;

import com.handong.oh318.model.ClassUml;
import com.handong.oh318.uml.BoxStyle;
import com.handong.oh318.uml.ElementAttributes;

import lombok.Data;

@Data
public class ClassBox {
        private String id;
        private String packageName;
        private String className;

        private BoxStyle style;

        private ElementAttributes swimLineAttr = new ElementAttributes(
                        "swimlane;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;html=1;fillStyle=auto;shadow=0;rounded=1;fillOpacity=100;strokeOpacity=100;separatorColor=none;noLabel=0;");
        private ElementAttributes inClassAttr = new ElementAttributes(
                        "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;");
        private ElementAttributes classNameAttr = new ElementAttributes(
                        "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;");

        public ClassBox(ClassUml uml, BoxStyle style) {
                this.id = uml.getUmlId();
                this.packageName = uml.getClassId().getPackageName();
                this.className = uml.getClassId().getName();
                this.style = style;
                this.swimLineAttr.addAttribute("fontStyle", style.getFontStyle().toString());
                this.swimLineAttr.addAttribute("fillColor", style.getBgColor());
                this.swimLineAttr.addAttribute("strokeColor", style.getBgColor());
                this.swimLineAttr.addAttribute("rounded", style.isBorderRounded() ? "1" : "0");
        }

}
