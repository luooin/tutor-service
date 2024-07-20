package com.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Info implements Serializable {
    private static final long serialVersionUID = 1L;

    private String teacherName;
    private String teacherAvatar;
    private String teacherPhone;
    private String teacherEmail;
    private Integer id;
    private Integer teacherId;
    private String code;
    private String birth;
    private String school;
    private String speciality;
    private String education;
    private String province;
    private String type;
    private String typeName;
    private String description;
    private String plan;
    private String star;
    private String level;
    private String status;
    private String reason;

}