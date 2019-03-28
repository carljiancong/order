package com.harmonycloud.enums;


public enum ErrorMsgEnum {

    SERVICE_ERROR("Internal service error"),
    PARAMETER_ERROR("Parameter error"),
    SAVE_ERROR("Save error"),
    UPDATE_ERROR("Update error"),
    FORMAT_ERROR("Unable to parse the proxy port number"),
    AUTHENTICATION_ERROR("Could not set user authentication in security context"),
    ROCKETMQ_ERROR("Rocketmq send message error"),
    OTHER_PERSON("The clinical note has been updated by another user");

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
