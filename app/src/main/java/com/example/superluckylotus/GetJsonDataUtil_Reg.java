package com.example.superluckylotus;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: GetJsonDataUtil_Reg
 * @packageName:com.example.superluckylotus
 * @description: 读取Json文件的工具类
 * @data: 2020.07.16 13:20
 **/

public class GetJsonDataUtil_Reg {
    public String getJson(Reg_infoActivity context, String fileName) {

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
