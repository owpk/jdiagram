package com.handong.oh318.drawio;

import java.util.UUID;

import com.handong.oh318.core.UmlIdAware;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UmlElement implements UmlIdAware {
    protected String umlId;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public UmlElement() {
        this.umlId = UUID.randomUUID().toString();
        this.x = -1;
        this.y = -1;
        this.width = -1;
        this.height = -1;
    }

    public UmlElement(UmlElement umlElement) {
        this.umlId = umlElement.umlId;
        this.x = umlElement.x;
        this.y = umlElement.y;
        this.width = umlElement.width;
        this.height = umlElement.height;
    }
}
