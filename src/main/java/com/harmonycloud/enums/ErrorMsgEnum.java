package com.harmonycloud.enums;

import org.apache.commons.lang.StringUtils;

public enum ErrorMsgEnum {


    SERVICE_ERROR("Internal service error"),
    PARAMETER_ERROR("Parameter error"),
    SAVE_ERROR("Save error");

    private String message;

    ErrorMsgEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
