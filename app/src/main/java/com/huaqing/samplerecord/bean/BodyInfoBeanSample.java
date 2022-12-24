package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BodyInfoBeanSample extends ResponseBean implements Serializable {


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

    public static class DataDTO implements Serializable {

        private String promptInfo;
        private Integer id;
        private Integer samplingTaskId;
        private int samplingRecordId;
        private String fieldName;
        private String fieldValue;
        private String fieldType;
        private String groupName;
        private Integer serialNum;
        private String dataSource;
        private List<SelectBean> selectBeans;

        public int getSamplingRecordId() {
            return samplingRecordId;
        }

        public void setSamplingRecordId(int samplingRecordId) {
            this.samplingRecordId = samplingRecordId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSamplingTaskId() {
            return samplingTaskId;
        }

        public void setSamplingTaskId(Integer samplingTaskId) {
            this.samplingTaskId = samplingTaskId;
        }

        public String getFieldValue() {
            return fieldValue == null ? "" : fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<SelectBean> getSelectBeans() {
            if (selectBeans == null) {
                return new ArrayList<>();
            }
            return selectBeans;
        }

        public void setSelectBeans(List<SelectBean> selectBeans) {
            this.selectBeans = selectBeans;
        }

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

        public String getDataSource() {
            return dataSource == null ? "" : dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getPromptInfo() {
            return promptInfo == null ? "" : promptInfo;
        }

        public void setPromptInfo(String promptInfo) {
            this.promptInfo = promptInfo;
        }
    }
}
