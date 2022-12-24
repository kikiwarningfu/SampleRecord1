package com.huaqing.samplerecord.bean;

import java.util.ArrayList;
import java.util.List;


public class UploadBean {

    private String msg;
    private Integer code;
    private List<String> data;

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<String> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
