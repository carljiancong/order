package com.harmonycloud.entity;

import javax.persistence.*;

@Entity
@Table(name = "prescription_drug")
public class PrescriptionDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer prescriptionDrugId;
    @Column(name = "drug_id")

    private Integer drugId;

    @Column(name = "regimen_line")
    private String regimenLine;

    @Column(name = "prescription_id")
    private Integer prescriptionId;

    public PrescriptionDrug() {
    }

    public PrescriptionDrug(Integer drugId, String regimenLine, Integer prescriptionId) {
        this.drugId = drugId;
        this.regimenLine = regimenLine;
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

    public String getRegimenLine() {
        return regimenLine;
    }

    public void setRegimenLine(String regimenLine) {
        this.regimenLine = regimenLine;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
}
