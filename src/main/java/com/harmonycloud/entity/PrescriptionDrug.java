package com.harmonycloud.entity;

import javax.persistence.*;

@Entity
@Table(name = "presciption_drug")
public class PrescriptionDrug {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer prescriptionDrugId;
    @Column(name = "drug_id")
    private Integer drugId;
    @Column(name = "reginmen_line")
    private String reginmenLine;
    @Column(name = "prescripttion_id")
    private Integer prescriptionId;

    public PrescriptionDrug() {
    }

    public PrescriptionDrug(Integer drugId, String reginmenLine, Integer prescriptionId) {
        this.drugId = drugId;
        this.reginmenLine = reginmenLine;
        this.prescriptionId = prescriptionId;
    }

    public Integer getPrescriptionDrugId() {
        return prescriptionDrugId;
    }

    public void setPrescriptionDrugId(Integer prescriptionDrugId) {
        this.prescriptionDrugId = prescriptionDrugId;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public String getReginmenLine() {
        return reginmenLine;
    }

    public void setReginmenLine(String reginmenLine) {
        this.reginmenLine = reginmenLine;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
}
