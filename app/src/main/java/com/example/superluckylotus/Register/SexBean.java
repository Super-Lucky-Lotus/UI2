package com.example.superluckylotus.Register;

import com.contrarywind.interfaces.IPickerViewData;


/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: SexBean
 * @packageName:com.example.superluckylotus
 * @description: 性别数据源
 * @data: 2020.07.16 00:19
 **/
public class SexBean implements IPickerViewData {
    int id;
    String sexNo;

    public SexBean(int id, String cardNo) {
        this.id = id;
        this.sexNo = cardNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return sexNo;
    }

    public void setCardNo(String cardNo) {
        this.sexNo = cardNo;
    }


    public String getPickerViewText() {
        return sexNo;
    }
}
