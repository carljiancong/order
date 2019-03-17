package com.harmonycloud.dto;

import com.harmonycloud.entity.PrescriptionDrug;

import java.util.Date;

public class Aduit {
    private Date date;
    private String severityCD;
    private String workstation;
    private String clientIp;
    private Integer userId;
    private String projectCode;
    private String correlationId;
    private String applicationName;
    private String infomation;

    public Aduit() {
    }

    public Aduit(Date date, String severityCD, String workstation, String clientIp, Integer userId, String projectCode, String correlationId, String applicationName, String infomation) {
        this.date = date;
        this.severityCD = severityCD;
        this.workstation = workstation;
        this.clientIp = clientIp;
        this.userId = userId;
        this.projectCode = projectCode;
        this.correlationId = correlationId;
        this.applicationName = applicationName;
        this.infomation = infomation;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSeverityCD() {
        return severityCD;
    }

    public void setSeverityCD(String severityCD) {
        this.severityCD = severityCD;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }
}

