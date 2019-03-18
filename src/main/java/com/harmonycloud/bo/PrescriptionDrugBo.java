package com.harmonycloud.bo;

public class PrescriptionDrugBo {
    private Integer prescriptionDrugId;
    private Integer drugId;
    private String tradeName;
    private String ingredient;
    private String regimenLine;
    private Integer prescriptionId;

    public PrescriptionDrugBo() {
    }

    public PrescriptionDrugBo(Integer prescriptionDrugId, Integer drugId, String tradeName, String ingredient, String regimenLine, Integer prescriptionId) {
        this.prescriptionDrugId = prescriptionDrugId;
        this.drugId = drugId;
        this.tradeName = tradeName;
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

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}
