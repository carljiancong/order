package com.harmonycloud.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(name = "处方id", example = "1")
    private Integer prescriptionId;
    @ApiModelProperty(name = "patient_id", example = "1")
    @Column(name = "patient_id")
    private Integer patientId;
    @ApiModelProperty(name = "会诊记录id", example = "1")
    @Column(name = "encounter_id")
    private Integer encounterId;
    @Column(name = "create_by")
    private String createBy;
    @ApiModelProperty(name = "create_date", example = "1")
    @Column(name = "create_date")
    private Date createDate;

    public Prescription() {
    }

    public Prescription(Integer patientId, Integer encounterId, String createBy, Date createDate) {
        this.patientId = patientId;
        this.encounterId = encounterId;
        this.createBy = createBy;
        this.createDate = createDate;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
