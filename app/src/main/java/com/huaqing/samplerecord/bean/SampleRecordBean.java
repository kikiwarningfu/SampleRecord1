package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class SampleRecordBean extends ResponseBean implements Serializable {
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
        private Integer samplingPlanId;
        private Integer userId;
        private Integer whichDay;
        private Integer whichTime;
        private String attachment;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getSamplingPlanId() {
            return samplingPlanId;
        }

        public void setSamplingPlanId(Integer samplingPlanId) {
            this.samplingPlanId = samplingPlanId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getWhichDay() {
            return whichDay;
        }

        public void setWhichDay(Integer whichDay) {
            this.whichDay = whichDay;
        }

        public Integer getWhichTime() {
            return whichTime;
        }

        public void setWhichTime(Integer whichTime) {
            this.whichTime = whichTime;
        }

        public String getAttachment() {
            return attachment == null ? "" : attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }
    }

//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        if (data == null) {
//            return new ArrayList<>();
//        }
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean implements Serializable {
//        /**
//         * id : 1
//         * taskId : 1
//         * codePre : hqhj-test01-1-1-1-
//         * dayNum : 1
//         * frequency : 1
//         * formData : {
//         * "bodyData": {
//         * "4564987431384613133": {
//         * "温度": 145674346461,
//         * "风向": "环境信息",
//         * "风速": "南北风"
//         * },
//         * "456498743138461321312133": {
//         * "温度": 145674346461,
//         * "风向": "环境信息",
//         * "风速": "南北风"
//         * }
//         * },
//         * "headData": {
//         * "tianqi信息": {
//         * "温度": 145674346461,
//         * "风向": "环境信息",
//         * "风速": "南北风"
//         * },
//         * "环境信息": {
//         * "温度": 145674346461,
//         * "风向": "环境信息",
//         * "风速": "南北风"
//         * }
//         * }
//         * }
//         */
//
//        private long id;
//        private long taskId;
//        private String codePre;
//        private int dayNum;
//        private int frequency;
//        private String formData;
//
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public long getTaskId() {
//            return taskId;
//        }
//
//        public void setTaskId(long taskId) {
//            this.taskId = taskId;
//        }
//
//        public String getCodePre() {
//            return codePre == null ? "" : codePre;
//        }
//
//        public void setCodePre(String codePre) {
//            this.codePre = codePre == null ? "" : codePre;
//        }
//
//        public int getDayNum() {
//            return dayNum;
//        }
//
//        public void setDayNum(int dayNum) {
//            this.dayNum = dayNum;
//        }
//
//        public int getFrequency() {
//            return frequency;
//        }
//
//        public void setFrequency(int frequency) {
//            this.frequency = frequency;
//        }
//
//        public String getFormData() {
//            return formData == null ? "" : formData;
//        }
//
//        public void setFormData(String formData) {
//            this.formData = formData == null ? "" : formData;
//        }
//    }
}
