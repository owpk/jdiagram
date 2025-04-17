package com.handong.oh318.drawio.model.box;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.handong.oh318.drawio.BoxStyle;
import com.handong.oh318.drawio.ElementAttributes;
import com.handong.oh318.drawio.UmlElement;
import com.handong.oh318.model.ClassUml;
import com.handong.oh318.model.FieldInfo;
import com.handong.oh318.model.MethodInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassBox extends UmlElement {
	private String classId;
	private String headerName;
	private BoxStyle style;

	private final ElementAttributes swimLineAttr = new ElementAttributes(
			"swimlane;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;html=1;fillStyle=auto;shadow=0;rounded=1;fillOpacity=100;strokeOpacity=100;separatorColor=none;noLabel=0;");

	private List<ClassBoxText> fields;
	private List<ClassBoxText> methods;

	public ClassBox(String umlId, ClassUml uml, BoxStyle style, int innerTextHeight) {
		this.umlId = umlId;
		this.classId = uml.getClassIdentity();
		this.headerName = style.getClassTypeIcons().get(uml.getType()) + " " + uml.getClassId().getName();
		this.style = style;
		this.fields = uml.getFields().stream().map(it -> this.createField(it, innerTextHeight))
				.collect(Collectors.toList());
		this.methods = uml.getMethods().stream().map(it -> this.createMethod(it, innerTextHeight))
				.collect(Collectors.toList());

		this.swimLineAttr.addAttribute("fillColor", style.getBgColor());
		this.swimLineAttr.addAttribute("strokeColor", style.getBorderColor());
		this.swimLineAttr.addAttribute("rounded", style.isBorderRounded() ? "1" : "0");

		this.width = Math.max(this.headerName.length(), this.width) * 5 + style.getMargin();

		Stream.concat(this.fields.stream(), this.methods.stream()).forEach(it -> it.setWidth(this.width));
	}

	private ClassBoxText createField(FieldInfo fieldInfo, int height) {
		var classBoxText = new ClassBoxText();
		classBoxText.setHeight(height);
		classBoxText.setText(style.getVisibilityIcons().get(fieldInfo.getVisibility()) + fieldInfo.getName() + ": "
				+ fieldInfo.getType());
		this.width = Math.max(this.width, classBoxText.getText().length());
		return classBoxText;
	}

	private ClassBoxText createMethod(MethodInfo methodInfo, int height) {
		var classBoxText = new ClassBoxText();
		classBoxText.setHeight(height);
		classBoxText.setText(style.getVisibilityIcons().get(methodInfo.getVisibility()) +
				methodInfo.getName() + "("
				+ methodInfo.getArgTypes().stream().map(it -> it.getType()).collect(Collectors.joining(","))
				+ ")" + ": " + methodInfo.getReturnType());

		this.width = Math.max(this.width, classBoxText.getText().length());
		return classBoxText;
	}

}
