package com.huaqing.samplerecord.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleInformationDetailBean extends ResponseBean{

    private DataDTO data;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private Integer id;
        private Integer taskId;
        private Integer pointId;
        private Integer noticeId;
        private String noticeCode;
        private String sampleCode;
        private String analyzedItem;
        private String samplingTime;
        private String status;
        private String entrustUnit;//受检
        private String entrustedUnit;//质检

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

        private Map<String, String> otherInfo;
        private List<FillContentBean> fillContentBeans;

        public static class FillContentBean implements Serializable {
            private String name;
            private String content;

            public FillContentBean() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public FillContentBean(String name, String content) {
                this.name = name;
                this.content = content;
            }
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public Integer getPointId() {
            return pointId;
        }

        public void setPointId(Integer pointId) {
            this.pointId = pointId;
        }

        public Integer getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(Integer noticeId) {
            this.noticeId = noticeId;
        }

        public String getNoticeCode() {
            return noticeCode == null ? "" : noticeCode;
        }

        public void setNoticeCode(String noticeCode) {
            this.noticeCode = noticeCode;
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

        public String getSamplingTime() {
            return samplingTime == null ? "" : samplingTime;
        }

        public void setSamplingTime(String samplingTime) {
            this.samplingTime = samplingTime;
        }

        public String getStatus() {
            return status == null ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Map<String, String> getOtherInfo() {
            return otherInfo;
        }

        public void setOtherInfo(Map<String, String> otherInfo) {
            this.otherInfo = otherInfo;
        }

        public List<FillContentBean> getFillContentBeans() {
            if (fillContentBeans == null) {
                return new ArrayList<>();
            }
            return fillContentBeans;
        }

        public void setFillContentBeans(List<FillContentBean> fillContentBeans) {
            this.fillContentBeans = fillContentBeans;
        }
    }
}
