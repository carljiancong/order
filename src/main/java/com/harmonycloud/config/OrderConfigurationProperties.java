package com.harmonycloud.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.net.URISyntaxException;

@ConfigurationProperties(prefix = "service")
public class OrderConfigurationProperties {
    private String drug;


    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public URI getDrugUri() {
        try {
            return new URI(drug);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
