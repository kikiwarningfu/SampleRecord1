package com.huaqing.samplerecord.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class SampleInformationPostBean extends ResponseBean implements Serializable {

    /**
     * data : {"head":{"萨达十大":[{"fieldName":"啊实打实的","serialNum":1,"fieldType":"0","groupName":"萨达十大"},{"fieldName":"大飒飒大","serialNum":2,"fieldType":"2","groupName":"萨达十大"}],"湿哒哒":[{"fieldName":"1","serialNum":1,"fieldType":"4","groupName":"湿哒哒"},{"fieldName":"2","serialNum":1,"fieldType":"1","groupName":"湿哒哒"}],"2321412412":[{"fieldName":"撒打算","serialNum":1,"fieldType":"3","groupName":"2321412412"}]},"body":[{"fieldName":"湿哒哒","serialNum":1,"fieldType":"5","groupName":"form_body"},{"fieldName":"大叔大婶","serialNum":1,"groupName":"form_body"},{"fieldName":"萨达十大三","serialNum":13,"groupName":"form_body"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * head : {"萨达十大":[{"fieldName":"啊实打实的","serialNum":1,"fieldType":"0","groupName":"萨达十大"},{"fieldName":"大飒飒大","serialNum":2,"fieldType":"2","groupName":"萨达十大"}],"湿哒哒":[{"fieldName":"1","serialNum":1,"fieldType":"4","groupName":"湿哒哒"},{"fieldName":"2","serialNum":1,"fieldType":"1","groupName":"湿哒哒"}],"2321412412":[{"fieldName":"撒打算","serialNum":1,"fieldType":"3","groupName":"2321412412"}]}
         * body : [{"fieldName":"湿哒哒","serialNum":1,"fieldType":"5","groupName":"form_body"},{"fieldName":"大叔大婶","serialNum":1,"groupName":"form_body"},{"fieldName":"萨达十大三","serialNum":13,"groupName":"form_body"}]
         */

        private HeadBean head;
        private List<BodyBean> body;

        public HeadBean getHead() {
            return head;
        }

        public void setHead(HeadBean head) {
            this.head = head;
        }

        public List<BodyBean> getBody() {
            if (body == null) {
                return new ArrayList<>();
            }
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public static class HeadBean implements Serializable {
            private List<萨达十大Bean> 萨达十大;
            private List<湿哒哒Bean> 湿哒哒;
            @SerializedName("2321412412")
            private List<_$2321412412Bean> _$2321412412;

            public List<萨达十大Bean> get萨达十大() {
                if (萨达十大 == null) {
                    return new ArrayList<>();
                }
                return 萨达十大;
            }

            public void set萨达十大(List<萨达十大Bean> 萨达十大) {
                this.萨达十大 = 萨达十大;
            }

            public List<湿哒哒Bean> get湿哒哒() {
                if (湿哒哒 == null) {
                    return new ArrayList<>();
                }
                return 湿哒哒;
            }

            public void set湿哒哒(List<湿哒哒Bean> 湿哒哒) {
                this.湿哒哒 = 湿哒哒;
            }

            public List<_$2321412412Bean> get_$2321412412() {
                if (_$2321412412 == null) {
                    return new ArrayList<>();
                }
                return _$2321412412;
            }

            public void set_$2321412412(List<_$2321412412Bean> _$2321412412) {
                this._$2321412412 = _$2321412412;
            }

            public static class 萨达十大Bean implements Serializable {
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
            }


            public static class 湿哒哒Bean implements Serializable {
                /**
                 * fieldName : 1
                 * serialNum : 1
                 * fieldType : 4
                 * groupName : 湿哒哒
                 */

                private String fieldName;
                private int serialNum;
                private String fieldType;
                private String groupName;

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
            }


            public static class _$2321412412Bean implements Serializable {
                /**
                 * fieldName : 撒打算
                 * serialNum : 1
                 * fieldType : 3
                 * groupName : 2321412412
                 */

                private String fieldName;
                private int serialNum;
                private String fieldType;
                private String groupName;

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
            }
        }


        public static class BodyBean implements Serializable {
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
        }
    }
}
