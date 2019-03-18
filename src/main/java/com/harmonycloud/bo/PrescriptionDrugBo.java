package com.harmonycloud.bo;

import javax.persistence.*;

public class PrescriptionDrugBo {
    private Integer prescriptionDrugId;
    private Integer drugId;
    private String drugName;
    private String ingredient;
    private String regimenLine;
    private Integer prescriptionId;

    public PrescriptionDrugBo() {
    }

    public PrescriptionDrugBo(Integer prescriptionDrugId, Integer drugId, String drugName, String ingredient, String regimenLine, Integer prescriptionId) {
        this.prescriptionDrugId = prescriptionDrugId;
        this.drugId = drugId;
        this.drugName = drugName;
        this.ingredient = ingredient;
        this.regimenLine = regimenLine;
        this.prescriptionId = prescriptionId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
