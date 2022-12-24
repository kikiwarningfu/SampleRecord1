package com.huaqing.samplerecord.bean;

public class SelectBean {
    private boolean isSelect;
    private String fileValue;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFileValue() {
        return fileValue == null ? "" : fileValue;
    }

    public void setFileValue(String fileValue) {
        this.fileValue = fileValue;
    }
}
