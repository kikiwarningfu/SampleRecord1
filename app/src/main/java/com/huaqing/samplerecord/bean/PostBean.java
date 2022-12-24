package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostBean {
    private String password;
    private String username;
    private String noticeCode;
    private String noticeId;
    private String status;
    private String token;
    private String headData;
    private String id;
    private String bodyData;
    private String code;
    private String taskId;
    private String dayNum;
    private String frequency;
    private String samplingPlanId;
    private String userId;
    private String whichDay;
    private String whichTime;
    private String attachment;
    private String planId;
    private String recordId;
    private List<BodyInfoBean> bodyInfoList;
    private String sampleCode;

    public String getSampleCode() {
        return sampleCode == null ? "" : sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public List<BodyInfoBean> getBodyInfoList() {
        if (bodyInfoList == null) {
            return new ArrayList<>();
        }
        return bodyInfoList;
    }

    public void setBodyInfoList(List<BodyInfoBean> bodyInfoList) {
        this.bodyInfoList = bodyInfoList;
    }

    public String getRecordId() {
        return recordId == null ? "" : recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPlanId() {
        return planId == null ? "" : planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    //    private List<String> attachment;
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
    public static class BodyInfoBean{
        private String fieldName;
        private String fieldValue;
        private String fieldType;
        private String promptInfo;
        private Integer serialNum;
        private String dataSource;
        private String samplingRecordId;
        private String id;

        public String getSamplingRecordId() {
            return samplingRecordId == null ? "" : samplingRecordId;
        }

        public void setSamplingRecordId(String samplingRecordId) {
            this.samplingRecordId = samplingRecordId;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
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
            return fieldType == null ? "" : fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getPromptInfo() {
            return promptInfo == null ? "" : promptInfo;
        }

        public void setPromptInfo(String promptInfo) {
            this.promptInfo = promptInfo;
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
    public static class DataBean {
        private String groupName;
        private List<DataBean.HeadInfoListDTO> headInfoList;

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<DataBean.HeadInfoListDTO> getHeadInfoList() {
            if (headInfoList == null) {
                return new ArrayList<>();
            }
            return headInfoList;
        }

        public void setHeadInfoList(List<DataBean.HeadInfoListDTO> headInfoList) {
            this.headInfoList = headInfoList;
        }

        public static class HeadInfoListDTO {

            private Integer id;
            private Integer samplingTaskId;
            private String fieldName;
            private String fieldValue;
            private String fieldType;
            private String groupName;
            private Integer serialNum;
            private String dataSource;

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

    public String getSamplingPlanId() {
        return samplingPlanId == null ? "" : samplingPlanId;
    }

    public void setSamplingPlanId(String samplingPlanId) {
        this.samplingPlanId = samplingPlanId;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWhichDay() {
        return whichDay == null ? "" : whichDay;
    }

    public void setWhichDay(String whichDay) {
        this.whichDay = whichDay;
    }

    public String getWhichTime() {
        return whichTime == null ? "" : whichTime;
    }

    public void setWhichTime(String whichTime) {
        this.whichTime = whichTime;
    }

    public String getAttachment() {
        return attachment == null ? "" : attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getNoticeId() {
        return noticeId == null ? "" : noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public static class ImageFileListBean implements Serializable {
        /**
         * id : 0
         * name : string
         * orderProcessId : 0
         * url : string
         */

        private int id;
        private String name;
        private int orderProcessId;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name == null ? "" : name;
        }

        public int getOrderProcessId() {
            return orderProcessId;
        }

        public void setOrderProcessId(int orderProcessId) {
            this.orderProcessId = orderProcessId;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url == null ? "" : url;
        }
    }

    private List<ImageFileListBean> imageFileList;

    public List<ImageFileListBean> getImageFileList() {
        if (imageFileList == null) {
            return new ArrayList<>();
        }
        return imageFileList;
    }

    public void setImageFileList(List<ImageFileListBean> imageFileList) {
        this.imageFileList = imageFileList;
    }

    public String getTaskId() {
        return taskId == null ? "" : taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? "" : taskId;
    }

    public String getDayNum() {
        return dayNum == null ? "" : dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum == null ? "" : dayNum;
    }

    public String getFrequency() {
        return frequency == null ? "" : frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency == null ? "" : frequency;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code == null ? "" : code;
    }

    public String getBodyData() {
        return bodyData == null ? "" : bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData == null ? "" : bodyData;
    }

    public String getHeadData() {
        return headData == null ? "" : headData;
    }

    public void setHeadData(String headData) {
        this.headData = headData == null ? "" : headData;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getNoticeCode() {
        return noticeCode == null ? "" : noticeCode;
    }

    public void setNoticeCode(String noticeCode) {
        this.noticeCode = noticeCode == null ? "" : noticeCode;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token == null ? "" : token;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username == null ? "" : username;
    }
}
