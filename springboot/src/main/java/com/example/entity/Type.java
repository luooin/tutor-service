package com.example.entity;

import java.io.Serializable;

/**
 * 分类表
 */
public class Type implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String type;
    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}