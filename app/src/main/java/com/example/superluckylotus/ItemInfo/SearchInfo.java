package com.example.superluckylotus.ItemInfo;

/**
 * @version: 3.0
 * @author: 黄坤
 * @packageName:com.example.superluckylotus
 * @description: 存储返回的search信息
 * @data: 2020.07.21 11:18
 **/

public class SearchInfo {

    private String username;
    private String time;
    private String tag;
    private String detail;
    private String cover_path;
    private String like_count;
    private String state;

    public SearchInfo(String name,String time,String tag,String detail,String cover_path,String like_count,String state) {
        this.username = name;
        this.time = time;
        this.cover_path = cover_path;
        this.detail = detail;
        this.like_count = like_count;
        this.tag = tag;
        this.state = state;
    }

    public String getName() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }

    public String getDetail() {
        return detail;
    }

    public String getCover_path() {
        return cover_path;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getState(){return state; }
}