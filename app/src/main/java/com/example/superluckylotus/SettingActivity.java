package com.example.superluckylotus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @author: 黄诗雯
 * @className: SettingActivity
 * @packageName:com.example.superluckylotus
 * @description: 设置界面
 * @data: 2020.07.11 22:30
 **/
public class SettingActivity extends AppCompatActivity {

    ImageButton mBackBtn;
    LinearLayout mChangename;
    LinearLayout mChangepassword;
    ImageButton mChangephoto;
    LinearLayout mChangeintro;
    LinearLayout mChangebirthday;
    LinearLayout mChangesex;
    LinearLayout mChangeaddress;
    TextView mTvSex;
    TextView mTvBirthday;
    TextView mTvAddress;
    private ArrayList<SexBean> sex = new ArrayList<SexBean>();

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private static boolean isLoaded = false;

    private OptionsPickerView pvCustomOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBackBtn=(ImageButton)findViewById(R.id.back);
        mChangeintro=(LinearLayout)findViewById(R.id.changeintro);
        mChangename=(LinearLayout)findViewById(R.id.changename);
        mChangepassword=(LinearLayout)findViewById(R.id.changepassword);
        mChangephoto=(ImageButton) findViewById(R.id.changephoto);
        mChangebirthday=(LinearLayout)findViewById(R.id.changebirthday);
        mChangesex=(LinearLayout)findViewById(R.id.changesex) ;
        mChangeaddress=(LinearLayout)findViewById(R.id.changeaddress);
        mTvSex=(TextView)findViewById(R.id.tv_sex);
        mTvBirthday=findViewById(R.id.tv_birthday);
        mTvAddress=findViewById(R.id.tv_address);
        setListeners();
        getSexData();
        initSexOptionPicker();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);//加载数据
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    //如果已创建就不再重新创建子线程了
                    if (thread == null) {

                        Toast.makeText(SettingActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(SettingActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(SettingActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                Toast.makeText(SettingActivity.this, tx, Toast.LENGTH_SHORT).show();
                mTvAddress.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    //解析数据
    private void initJsonData() {
        /**
         *assets 目录下的Json文件
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    //Gson 解析
    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void initSexOptionPicker() {
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = sex.get(options1).getPickerViewText();
                mTvSex.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_sex_option, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSexData();
                                pvCustomOptions.setPicker(sex);
                            }
                        });

                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();
        pvCustomOptions.setPicker(sex);//添加数据
    }

    private void getSexData() {
        sex.add(new SexBean(0, "男 " ));
        sex.add(new SexBean(0, "女" ));
        sex.add(new SexBean(0, "不显示 " ));

        for (int i = 0; i < sex.size(); i++) {
            if (sex.get(i).getCardNo().length() > 3) {
                String str_item = sex.get(i).getCardNo().substring(0, 3) ;
                sex.get(i).setCardNo(str_item);
            }
        }
    }

    private void setListeners(){
        SettingActivity.OnClick onClick = new SettingActivity.OnClick();
        mBackBtn.setOnClickListener(onClick);
        mChangeintro.setOnClickListener(onClick);
        mChangephoto.setOnClickListener(onClick);
        mChangename.setOnClickListener(onClick);
        mChangepassword.setOnClickListener(onClick);
        mChangebirthday.setOnClickListener(onClick);
        mChangesex.setOnClickListener(onClick);
        mChangeaddress.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch (v.getId()){
                case R.id.back:
                    intent = new Intent(SettingActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changephoto:
                    intent = new Intent(SettingActivity.this,ChangePhotoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changepassword:
                    intent = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changename:
                    intent = new Intent(SettingActivity.this,ChangeNameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changeintro:
                    intent = new Intent(SettingActivity.this,ChangeIntroductionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changebirthday:
                    DatePickerDialog datePickerDialog=new DatePickerDialog(SettingActivity.this, new DatePickerDialog.OnDateSetListener() {
              @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                             Toast.makeText(SettingActivity.this, "您当前选择日期："+year+"年"+(month+1)+"月"+dayOfMonth+"日", Toast.LENGTH_SHORT).show();

                  mTvBirthday.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");              }
            }, 2020, 7,14);
                             datePickerDialog.show();//展示日期对话框

                     break;
                case R.id.changesex:
                    if(pvCustomOptions != null){
                        pvCustomOptions.show();
                    }
                    break;
                case R.id.changeaddress:
                    if (isLoaded) {
                        showPickerView();
                    } else {
                        Toast.makeText(SettingActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                    }
                    break;


                default:
                    break;

            }

        }








    }
}