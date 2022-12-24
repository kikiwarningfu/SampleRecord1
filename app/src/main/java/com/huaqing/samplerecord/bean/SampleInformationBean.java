package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleInformationBean implements Serializable{
    private List<SampleInfoTitleBean> sampleInfoTitleBeans;

    public List<SampleInfoTitleBean> getSampleInfoTitleBeans() {
        return sampleInfoTitleBeans;
    }

    public void setSampleInfoTitleBeans(List<SampleInfoTitleBean> sampleInfoTitleBeans) {
        this.sampleInfoTitleBeans = sampleInfoTitleBeans;
    }

    public static class SampleInfoTitleBean implements Serializable{
        private String title;
        private Map<String, String> titleValueList;
        private boolean isSelect;
        private List<FillContentBean> fillContentBeans;
        private List<BottomContentBean> bottomContentBeans;

        public List<BottomContentBean> getBottomContentBeans() {
            if (bottomContentBeans == null) {
                return new ArrayList<>();
            }
            return bottomContentBeans;
        }

        public void setBottomContentBeans(List<BottomContentBean> bottomContentBeans) {
            this.bottomContentBeans = bottomContentBeans;
        }

        public List<FillContentBean> getFillContentBeans() {
            return fillContentBeans;
        }

        public void setFillContentBeans(List<FillContentBean> fillContentBeans) {
            this.fillContentBeans = fillContentBeans;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Map<String, String> getTitleValueList() {
            return titleValueList;
        }

        public void setTitleValueList(Map<String, String> titleValueList) {
            this.titleValueList = titleValueList;
        }


        public static class FillContentBean implements Serializable{
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

        public static class BottomContentBean implements Serializable {
            private String name;
            private String content;
            private String code;

            public String getCode() {
                return code == null ? "" : code;
            }

            public void setCode(String code) {
                this.code = code == null ? "" : code;
            }

            public BottomContentBean() {


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

            public BottomContentBean(String name, String content) {
                this.name = name;
                this.content = content;
            }
        }
    }


}
