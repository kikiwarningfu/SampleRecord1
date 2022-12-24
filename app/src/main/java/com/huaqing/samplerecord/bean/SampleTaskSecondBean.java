package com.huaqing.samplerecord.bean;

import java.util.ArrayList;
import java.util.List;



public class SampleTaskSecondBean extends ResponseBean {


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
        private String id;
        private String pointName;
        private String pointAddress;
        private String itemGroup;
        private Integer frequency;
        private Integer dayNum;
        private String description;
        private String firstTime;
        private String finalTime;
        private String status;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPointName() {
            return pointName == null ? "" : pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPointAddress() {
            return pointAddress == null ? "" : pointAddress;
        }

        public void setPointAddress(String pointAddress) {
            this.pointAddress = pointAddress;
        }

        public String getItemGroup() {
            return itemGroup == null ? "" : itemGroup;
        }

        public void setItemGroup(String itemGroup) {
            this.itemGroup = itemGroup;
        }

        public Integer getFrequency() {
            return frequency;
        }

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }

        public Integer getDayNum() {
            return dayNum;
        }

        public void setDayNum(Integer dayNum) {
            this.dayNum = dayNum;
        }

        public String getDescription() {
            return description == null ? "" : description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFirstTime() {
            return firstTime == null ? "" : firstTime;
        }

        public void setFirstTime(String firstTime) {
            this.firstTime = firstTime;
        }

        public String getFinalTime() {
            return finalTime == null ? "" : finalTime;
        }

        public void setFinalTime(String finalTime) {
            this.finalTime = finalTime;
        }

        public String getStatus() {
            return status == null ? "1" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
