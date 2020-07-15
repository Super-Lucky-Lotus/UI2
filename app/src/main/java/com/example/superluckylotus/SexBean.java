package com.example.superluckylotus;

import com.contrarywind.interfaces.IPickerViewData;

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
