package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GetBodyInfoBean extends ResponseBean implements Serializable {

    private List<DataDTO> data;

    public List<DataDTO> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable{
        private String fieldName;
        private String fieldType;
        private Integer serialNum;
        private String promptInfo;
        private String dataSource;

        public String getFieldName() {
            return fieldName == null ? "" : fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldType() {
            return fieldType == null ? "" : fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public Integer getSerialNum() {
            return serialNum;
        }

        public void setSerialNum(Integer serialNum) {
            this.serialNum = serialNum;
        }

        public String getPromptInfo() {
            return promptInfo == null ? "" : promptInfo;
        }

        public void setPromptInfo(String promptInfo) {
            this.promptInfo = promptInfo;
        }

        public String getDataSource() {
            return dataSource == null ? "" : dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }
    }
}
