package com.handong.oh318;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class JavaClassSourceBox extends JavaSourceBox<JavaClassSource> {

    public JavaClassSourceBox(JavaClassSource src) {
        super(src);
    }

    @Override
    public void setWidthAndHeight() {
        maxLength = src.getName().length();
        // box.setLineCount(box.getLineCount() + 1);
        this.lineCount += 1;

        nameBoxInfo.setLineCount(nameBoxInfo.getLineCount() + 1);

        for (FieldSource<JavaClassSource> field : src.getFields()) {
            maxLength = Math.max(maxLength, field.getName().length());
            // box.setLineCount(box.getLineCount() + 1);
            this.lineCount += 1;
            fieldsBoxInfo.setLineCount(fieldsBoxInfo.getLineCount() + 1);
        }

        for (MethodSource<JavaClassSource> method : src.getMethods()) {

            maxLength = Math.max(maxLength, method.getName().length());
            this.lineCount += 1;
            methodsBoxInfo.setLineCount(methodsBoxInfo.getLineCount() + 1);
        }

        int nameBoxH = nameBoxInfo.getLineCount() * 26;
        int fiedlBoxH = fieldsBoxInfo.getLineCount() * 26;
        int methodBoxH = methodsBoxInfo.getLineCount() * 26;

        nameBoxInfo.setHeight(nameBoxH);
        fieldsBoxInfo.setHeight(fiedlBoxH);
        methodsBoxInfo.setHeight(methodBoxH);
        this.setHeight(nameBoxH + fiedlBoxH + methodBoxH + 8);

        int width = 225;
        nameBoxInfo.setWidth(width);
        fieldsBoxInfo.setWidth(width);
        methodsBoxInfo.setWidth(width);
        this.setWidth(width);
    }

    @Override
    public void setCoordinate(int idx, int maxWidth, int maxHeight) {
        this.setX((idx % 3 + 1) * 70 + (idx % 3) * maxWidth / 2);
        this.setY((idx / 3 + 1) * 80 + (idx / 3) * maxHeight);

        nameBoxInfo.setX(this.getX());
        nameBoxInfo.setY(this.getY());

        fieldsBoxInfo.setX(this.getX());
        fieldsBoxInfo.setY(nameBoxInfo.getHeight());

        methodsBoxInfo.setX(this.getX());
        methodsBoxInfo.setY(fieldsBoxInfo.getY() + nameBoxInfo.getHeight() + 8);
    }

}
