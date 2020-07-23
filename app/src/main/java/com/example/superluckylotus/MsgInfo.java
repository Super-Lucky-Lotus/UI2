package com.example.superluckylotus;

public class MsgInfo {
    private String name;
    private String kind;
    private String time;

    public MsgInfo(String name, String time, String kind) {
        this.name = name;
        this.time = time;
        this.kind = kind;
    }
    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }
    public String getKind() {
        return kind;
    }
}
