package com.huaqing.samplerecord.bean;

import java.util.ArrayList;
import java.util.List;


public class TaskHeadInfoListBean extends ResponseBean{


    private List<DataBean> data;

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String groupName;
        private List<HeadInfoListDTO> headInfoList;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<HeadInfoListDTO> getHeadInfoList() {
            if (headInfoList == null) {
                return new ArrayList<>();
            }
            return headInfoList;
        }

        public void setHeadInfoList(List<HeadInfoListDTO> headInfoList) {
            this.headInfoList = headInfoList;
        }

        public static class HeadInfoListDTO {

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
            private String promptInfo;

            public int getSamplingRecordId() {
                return samplingRecordId;
            }

            public void setSamplingRecordId(int samplingRecordId) {
                this.samplingRecordId = samplingRecordId;
            }

            public String getPromptInfo() {
                return promptInfo == null ? "" : promptInfo;
            }

            public void setPromptInfo(String promptInfo) {
                this.promptInfo = promptInfo;
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

            public String getFieldName() {
                return fieldName == null ? "" : fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }

            public String getFieldValue() {
                return fieldValue == null ? "" : fieldValue;
            }

            public void setFieldValue(String fieldValue) {
                this.fieldValue = fieldValue;
            }

            public String getFieldType() {
                return fieldType == null ? "0" : fieldType;
            }

            public void setFieldType(String fieldType) {
                this.fieldType = fieldType;
            }

            public String getGroupName() {
                return groupName == null ? "" : groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
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
        }
    }
}
