package com.harmonycloud.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer prescriptionId;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "encounter_id")
    private Integer encounterId;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "clinic_id")
    private Integer clinicId;
    @Column(name = "clinic_name")
    private String clinicName;


    public Prescription() {
    }

    public Prescription(Integer patientId, Integer encounterId, String createBy, Date createDate, Integer clinicId, String clinicName) {
        this.patientId = patientId;
        this.encounterId = encounterId;
        this.createBy = createBy;
        this.createDate = createDate;
        this.clinicId = clinicId;
        this.clinicName = clinicName;
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

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}
