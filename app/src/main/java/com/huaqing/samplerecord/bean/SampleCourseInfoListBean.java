package com.huaqing.samplerecord.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SampleCourseInfoListBean extends ResponseBean implements Serializable {

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

    public static class DataBean implements Serializable {
        /**
         * code : ad760a3d-199e-44db-bcce-50a9af0680cd
         * conf : {"大叔大婶":"第二个","萨达十大三":"第三个","湿哒哒":"第一个"}
         */

        @SerializedName("code")
        private String codeX;
        private Map<String, String> conf;
        private String bianhao;
        private String projectName;

        public String getBianhao() {
            return bianhao == null ? "" : bianhao;
        }

        public void setBianhao(String bianhao) {
            this.bianhao = bianhao == null ? "" : bianhao;
        }

        public String getProjectName() {
            return projectName == null ? "" : projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName == null ? "" : projectName;
        }

        public String getCodeX() {
            return codeX == null ? "" : codeX;
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX == null ? "" : codeX;
        }

        public Map<String, String> getConf() {
            return conf;
        }

        public void setConf(Map<String, String> conf) {
            this.conf = conf;
        }
    }
}
