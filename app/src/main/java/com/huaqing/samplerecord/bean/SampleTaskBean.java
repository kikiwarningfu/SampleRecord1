package com.huaqing.samplerecord.bean;


public class SampleTaskBean extends ResponseBean{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private Integer id;
        private Integer samplingPlanId;

        private Integer userId;

        private Integer whichDay;

        private Integer whichTime;

        private String attachment;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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
}
