package com.huaqing.samplerecord.bean;

import java.io.Serializable;
import java.util.Map;


public class UpdateSampleInformationBean extends ResponseBean implements Serializable {


    /**
     * data : {"固定剂":"硫酸","样品编号":"hqkj-1-1-1-1-1","采样介质":"G瓶","分析项目":"总磷"}
     */
    private Map<String,String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
