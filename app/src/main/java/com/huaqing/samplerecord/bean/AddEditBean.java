package com.huaqing.samplerecord.bean;


public class AddEditBean extends ResponseBean{

    private String data;

    public String getData() {
        return data == null ? "" : data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
