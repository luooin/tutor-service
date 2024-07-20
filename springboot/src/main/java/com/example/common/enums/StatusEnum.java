package com.example.common.enums;

public enum StatusEnum {

    CHECKING("待审核"),
    CHECK_OK("审核通过"),
    CHECK_NO("审核不通过"),
    RESERVE_OK("已接受"),
    RESERVE_NO("已拒绝"),
    RESERVING("待确认"),
    ;
    public String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
