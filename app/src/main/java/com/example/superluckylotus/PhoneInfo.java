package com.example.superluckylotus;

/**
 * @version: 3.0
 * @author: 宋佳容
 * @className: Phone
 * @packageName:com.example.superluckylotus
 * @description: 调用通讯录联系人信息
 * @data: 2020.07.21 11:18
 **/

public class PhoneInfo {
    private String name;
    private String phone;
    private String sortKey;
    private int id;

    public PhoneInfo(String name, String phone, String sortKey, int id) {
        this.name = name;
        this.phone=phone;
        this.sortKey = sortKey;
        this.id = id;
    }
    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}