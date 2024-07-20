package com.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Information implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String img;
    private String name;
    private String content;
    private String time;

    private int count;

}