package com.powernode.util;

public enum ResultEnum {
    SUCCESS_NOTDATA(200,"SUCCESS"),
    SUCCESS_DATA(200,"SUCCESS"),
    NOTFOUND_DATA(404,"未找到数据"),



    ;

    private int status;
    private String message;

    ResultEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
