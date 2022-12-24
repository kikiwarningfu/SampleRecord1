package com.huaqing.samplerecord.bean;

public class ShowSuccessEvent {

    private String ids;
    private String tvTitle;

    public ShowSuccessEvent() {
    }

    public ShowSuccessEvent(String ids, String tvTitle) {
        this.ids = ids;
        this.tvTitle = tvTitle;
    }

    public String getIds() {
        return ids == null ? "" : ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTvTitle() {
        return tvTitle == null ? "" : tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }
}
