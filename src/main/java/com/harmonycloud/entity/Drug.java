package com.harmonycloud.entity;

public class Drug {

    private Integer drugId;

    private String ingredient;

    private String tradeName;

    public Drug() {
    }

    public Drug(Integer drugId, String ingredient, String tradeName) {
        this.drugId = drugId;
        this.ingredient = ingredient;
        this.tradeName = tradeName;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}
