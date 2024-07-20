package com.example.entity;

import lombok.Data;

/**
 * 预约实体
 */

@Data
public class Reserve {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer teacherId;
    private String start;
    private String end;
    private String content;
    private String status;

    private String userName;
    private String userPhone;
    private String teacherName;
}
