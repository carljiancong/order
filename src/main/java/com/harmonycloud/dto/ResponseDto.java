package com.harmonycloud.dto;

import com.harmonycloud.entity.Drug;

import java.util.List;

public class ResponseDto {
    private List<Drug> durgList;

    public ResponseDto() {
    }

    public ResponseDto(List<Drug> durgList) {
        this.durgList = durgList;
    }


    public List<Drug> getDurgList() {
        return durgList;
    }

    public void setDurgList(List<Drug> durgList) {
        this.durgList = durgList;
    }


}
