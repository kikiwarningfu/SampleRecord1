package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TaskListBean extends ResponseBean implements Serializable {


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
        private Integer noticeId;
        private Integer taskId;
        private String noticeCode;
        private String entrustUnit;
        private String entrustedUnit;
        private String address;
        private String entranceTimeRequire;
//        private List<SamplingPlanInfoListDTO> samplingPlanInfoList;

        public Integer getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(Integer noticeId) {
            this.noticeId = noticeId;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public String getNoticeCode() {
            return noticeCode == null ? "" : noticeCode;
        }

        public void setNoticeCode(String noticeCode) {
            this.noticeCode = noticeCode;
        }

        public String getEntrustUnit() {
            return entrustUnit == null ? "" : entrustUnit;
        }

        public void setEntrustUnit(String entrustUnit) {
            this.entrustUnit = entrustUnit;
        }

        public String getEntrustedUnit() {
            return entrustedUnit == null ? "" : entrustedUnit;
        }

        public void setEntrustedUnit(String entrustedUnit) {
            this.entrustedUnit = entrustedUnit;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEntranceTimeRequire() {
            return entranceTimeRequire == null ? "" : entranceTimeRequire;
        }

        public void setEntranceTimeRequire(String entranceTimeRequire) {
            this.entranceTimeRequire = entranceTimeRequire;
        }

//        public List<SamplingPlanInfoListDTO> getSamplingPlanInfoList() {
//            if (samplingPlanInfoList == null) {
//                return new ArrayList<>();
//            }
//            return samplingPlanInfoList;
//        }
//
//        public void setSamplingPlanInfoList(List<SamplingPlanInfoListDTO> samplingPlanInfoList) {
//            this.samplingPlanInfoList = samplingPlanInfoList;
//        }
//
//        public static class SamplingPlanInfoListDTO {
//            private String pointName;
//            private String pointAddress;
//            private String itemGroup;
//            private Integer frequency;
//            private Integer dayNum;
//            private String description;
//            private String firstTime;
//            private String finalTime;
//
//        }
    }
}
