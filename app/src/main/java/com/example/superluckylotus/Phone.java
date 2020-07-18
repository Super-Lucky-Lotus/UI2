package com.example.superluckylotus;

import android.app.Application;

public class Phone extends Application {
    private String phone = "";

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
