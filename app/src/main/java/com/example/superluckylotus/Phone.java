package com.example.superluckylotus;

import android.app.Application;

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: Phone
 * @packageName:com.example.superluckylotus
 * @description: 调用通讯录手机号码信息
 * @data: 2020.07.21 11:18
 **/

public class Phone extends Application {
    private String phone = "";

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
