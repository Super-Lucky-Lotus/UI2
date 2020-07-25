package com.example.superluckylotus.ItemInfo;

/**
 * @version: 3.0
 * @author: 黄坤
 * @packageName:com.example.superluckylotus
 * @description: 存储返回的图片信息
 * @data: 2020.07.21 11:18
 **/

public class ImageInfo {
    private String image_url1 = "";
    private String image_url2 = "";
    private String image_url3 = "";

    public ImageInfo(String url1,String url2,String url3) {
        image_url1 = url1;
        image_url2 = url2;
        image_url3 = url3;
    }
    public String getImage_url1() {
        return image_url1;
    }
    public String getImage_url2() {
        return image_url2;
    }
    public String getImage_url3() {
        return image_url3;
    }

}