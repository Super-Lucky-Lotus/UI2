package com.example.superluckylotus.Manager;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: GetJsonDataUtil
 * @packageName:com.example.superluckylotus
 * @description: 读取Json文件的工具类
 * @data: 2020.07.17 20:59
 **/

public class GetJsonDataUtil_Post {


    public String getJson(VideoPostActivity context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
