package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class SampleInformationListBean extends ResponseBean implements Serializable {


    /**
     * data : {"head":[{"groupName":"萨达十大","conf":[{"fieldName":"啊实打实的","serialNum":1,"fieldType":"0","groupName":"萨达十大"},{"fieldName":"大飒飒大","serialNum":2,"fieldType":"2","groupName":"萨达十大"}]},{"groupName":"湿哒哒","conf":[{"fieldName":"1","serialNum":1,"fieldType":"4","groupName":"湿哒哒"},{"fieldName":"2","serialNum":1,"fieldType":"1","groupName":"湿哒哒"}]},{"groupName":"2321412412","conf":[{"fieldName":"撒打算","serialNum":1,"fieldType":"3","groupName":"2321412412"}]}],"body":[{"fieldName":"湿哒哒","serialNum":1,"fieldType":"5","groupName":"form_body"},{"fieldName":"大叔大婶","serialNum":1,"groupName":"form_body"},{"fieldName":"萨达十大三","serialNum":13,"groupName":"form_body"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private List<HeadDataBean> headData;
        private List<BodyDataBean> bodyData;


        public List<HeadDataBean> getHeadData() {
            if (headData == null) {
                return new ArrayList<>();
            }
            return headData;
        }

        public void setHeadData(List<HeadDataBean> headData) {
            this.headData = headData;
        }

        public List<BodyDataBean> getBodyData() {
            if (bodyData == null) {
                return new ArrayList<>();
            }
            return bodyData;
        }

        public void setBodyData(List<BodyDataBean> bodyData) {
            this.bodyData = bodyData;
        }

        public static class HeadDataBean implements Serializable {
            /**
             * groupName : 萨达十大
             * conf : [{"fieldName":"啊实打实的","serialNum":1,"fieldType":"0","groupName":"萨达十大"},{"fieldName":"大飒飒大","serialNum":2,"fieldType":"2","groupName":"萨达十大"}]
             */

            private String groupName;
            private List<ConfBean> conf;
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
                this.groupName = groupName == null ? "" : groupName;
            }

            public List<ConfBean> getConf() {
                if (conf == null) {
                    return new ArrayList<>();
                }
                return conf;
            }

            public void setConf(List<ConfBean> conf) {
                this.conf = conf;
            }

            public static class ConfBean implements Serializable {
                /**
                 * fieldName : 啊实打实的
                 * serialNum : 1
                 * fieldType : 0
                 * groupName : 萨达十大
                 */

                private String fieldName;
                private int serialNum;
                private String fieldType;
                private String groupName;
                private String value;

                public String getFieldName() {
                    return fieldName == null ? "" : fieldName;
                }

                public void setFieldName(String fieldName) {
                    this.fieldName = fieldName == null ? "" : fieldName;
                }

                public int getSerialNum() {
                    return serialNum;
                }

                public void setSerialNum(int serialNum) {
                    this.serialNum = serialNum;
                }

                public String getFieldType() {
                    return fieldType == null ? "" : fieldType;
                }

                public void setFieldType(String fieldType) {
                    this.fieldType = fieldType == null ? "" : fieldType;
                }

                public String getGroupName() {
                    return groupName == null ? "" : groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName == null ? "" : groupName;
                }

                public String getValue() {
                    return value == null ? "" : value;
                }

                public void setValue(String value) {
                    this.value = value == null ? "" : value;
                }
            }
        }


        public static class BodyDataBean implements Serializable {
            /**
             * fieldName : 湿哒哒
             * serialNum : 1
             * fieldType : 5
             * groupName : form_body
             */

            private String fieldName;
            private int serialNum;
            private String fieldType;
            private String groupName;
            private String value;
            private String code;

            public String getCode() {
                return code == null ? "" : code;
            }

            public void setCode(String code) {
                this.code = code == null ? "" : code;
            }

            public String getFieldName() {
                return fieldName == null ? "" : fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName == null ? "" : fieldName;
            }

            public int getSerialNum() {
                return serialNum;
            }

            public void setSerialNum(int serialNum) {
                this.serialNum = serialNum;
            }

            public String getFieldType() {
                return fieldType == null ? "" : fieldType;
            }

            public void setFieldType(String fieldType) {
                this.fieldType = fieldType == null ? "" : fieldType;
            }

            public String getGroupName() {
                return groupName == null ? "" : groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName == null ? "" : groupName;
            }

            public String getValue() {
                return value == null ? "" : value;
            }

            public void setValue(String value) {
                this.value = value == null ? "" : value;
            }
        }
    }
}
