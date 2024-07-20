package com.example.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class Publish implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private String name;
    private String content;
    private String time;
    private Integer teacherId;
    private String status;

    private String teacherName;
    private String teacherPhone;
    private String userName;
    private String userPhone;

}