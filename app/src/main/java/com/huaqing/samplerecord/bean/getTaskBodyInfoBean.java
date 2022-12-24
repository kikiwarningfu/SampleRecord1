package com.huaqing.samplerecord.bean;

import java.util.ArrayList;
import java.util.List;



public class getTaskBodyInfoBean extends ResponseBean{


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

    public static class DataDTO {
        private Integer id;
        private Integer samplingTaskId;
        private String sampleCode;
        private String analyzedItem;

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

        public String getSampleCode() {
            return sampleCode == null ? "" : sampleCode;
        }

        public void setSampleCode(String sampleCode) {
            this.sampleCode = sampleCode;
        }

        public String getAnalyzedItem() {
            return analyzedItem == null ? "" : analyzedItem;
        }

        public void setAnalyzedItem(String analyzedItem) {
            this.analyzedItem = analyzedItem;
        }
    }
}
