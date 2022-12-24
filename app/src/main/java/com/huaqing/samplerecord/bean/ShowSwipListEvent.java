package com.huaqing.samplerecord.bean;

public class ShowSwipListEvent {

    private String ids;
    private String tvTitle;

    public ShowSwipListEvent() {
    }

    public ShowSwipListEvent(String ids, String tvTitle) {
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
