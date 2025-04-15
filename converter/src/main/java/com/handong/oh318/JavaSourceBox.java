package com.handong.oh318;

import org.jboss.forge.roaster.model.source.JavaSource;

public abstract class JavaSourceBox<T extends JavaSource<T>> extends Box {
    protected T src;//
    protected Box nameBoxInfo; //
    protected Box fieldsBoxInfo; //
    protected Box methodsBoxInfo; //

    protected String _extends = "";
    protected String _interface = "";
    protected int classId; //

    protected int maxLength = 0;

    JavaSourceBox() {
        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();
    }

    // relationship info.
    /**
     * get maxLength from name, fields, methods
     * => this.setWidth()
     * 
     * count of name, fields, method
     * => this.setHeight()
     * 
     * with, height of name, filed, method
     */

    public JavaSourceBox(T src) {
        this.src = src;
        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();
        this.setWidthAndHeight();
    }

    public abstract void setWidthAndHeight();

    public abstract void setCoordinate(int idx, int maxWidth, int maxHeight);

    public void setExtends(String ext) {
        this._extends = ext;
    }

    public void setInterface(String interf) {
        this._interface = this._interface.concat(interf);
        // TODO Several interfaces
    }

    public void setID(int id) {
        this.classId = id;
    }

    public String getExtends() {
        return this._extends;
    }

    public String getInterface() {
        return this._interface;
    }

    public void setJavaClassSource(T src) {
        this.src = src;
    }

    public T getJavaClassSource() {
        return this.src;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getClassId() {
        return this.classId;
    }

    public void setFieldsBoxInfo(Box fieldsBoxInfo) {
        this.fieldsBoxInfo = fieldsBoxInfo;
    }

    public Box getFieldboxInfo() {
        return this.fieldsBoxInfo;
    }

    public void setMethodsBoxInfo(Box methodsBoxInfo) {
        this.methodsBoxInfo = methodsBoxInfo;
    }

    public Box getMethodsBoxInfo() {
        return this.methodsBoxInfo;
    }
}
