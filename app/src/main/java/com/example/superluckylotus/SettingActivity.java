package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: 3.0
 * @author: 黄诗雯
 * @className: SettingActivity
 * @packageName:com.example.superluckylotus
 * @description: 设置界面
 * @data: 2020.07.16 13:25
 **/

/**
 * @version: 3.0
 * @author: 黄坤
 * @className: SettingActivity
 * @packageName:com.example.superluckylotus
 * @description: 对应用户的信息显示
 * @data: 2020.07.16 17:25
 **/
public class SettingActivity extends AppCompatActivity {

    protected String nickname="用户名";
    protected String gender ="";
    protected String password = "0";
    protected String region = "";
    protected String birthday = "";
    protected String description = "";
    protected String photo = "";
    ImageButton mBackBtn;
    LinearLayout mChangename;
    LinearLayout mChangepassword;
    MyImageView mChangephoto;
    LinearLayout mChangeintro;
    LinearLayout mChangebirthday;
    LinearLayout mChangesex;
    LinearLayout mChangeaddress;
    Button mPreserve;
    protected TextView textView1;
    protected TextView textView2;
    protected TextView textView3;
    protected TextView textView4;
    protected TextView textView5;
    protected TextView textView6;
    protected MyImageView My_pic;
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
        textView1 = (TextView)findViewById(R.id.textView6);
        textView2 = (TextView)findViewById(R.id.textView8);
        textView3 = (TextView)findViewById(R.id.tv_sex);
        textView4 = (TextView)findViewById(R.id.tv_address);
        textView5 = (TextView)findViewById(R.id.textView18);
        textView6 = (TextView)findViewById(R.id.tv_birthday);
        My_pic = (MyImageView) findViewById(R.id.changephoto);
        mBackBtn=(ImageButton)findViewById(R.id.back);
        mChangeintro=(LinearLayout)findViewById(R.id.changeintro);
        mChangename=(LinearLayout)findViewById(R.id.changename);
        mChangepassword=(LinearLayout)findViewById(R.id.changepassword);
        mChangephoto=(MyImageView) findViewById(R.id.changephoto);
        mChangebirthday=(LinearLayout)findViewById(R.id.changebirthday);
        mChangesex=(LinearLayout)findViewById(R.id.changesex) ;
        mChangeaddress=(LinearLayout)findViewById(R.id.changeaddress);
        mPreserve=(Button)findViewById(R.id.btn_preserve);
        mTvSex=(TextView)findViewById(R.id.tv_sex);
        mTvBirthday=findViewById(R.id.tv_birthday);
        mTvAddress=findViewById(R.id.tv_address);
        getData();
        setListeners();
        getSexData();
        initSexOptionPicker();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);//加载数据
    }

    private void getData() {
        Phone phoneObj = (Phone)getApplication();
        final String phone = phoneObj.getPhone();
        String path = "http://139.219.4.34/profile/";
        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
        userParams.put("phone", phone);

        if(!phone.equals("")) {
            HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                @Override
                public void onSuccess(String result) throws JSONException {
                    JSONObject result_json = new JSONObject(result);
                    String get = result_json.getString("msg");
                    Log.v("SettingActivity",result);
                    if (get.equals("success")) {
                        photo = result_json.getString("face_image");
                        nickname = result_json.getString("nickname");
                        password = result_json.getString("password");
                        gender= result_json.getString("gender");
                        region = result_json.getString("region");
                        description = result_json.getString("description");
                        birthday = result_json.getString("birthday");
                        if(!photo.equals("")) {
                            My_pic.setImageURL(photo);
                        }
                        textView1.setText(nickname);
                        textView2.setText(password);
                        textView3.setText(gender);
                        textView4.setText(region);
                        textView5.setText(description);
                        textView6.setText(birthday);
                    }
                }

                @Override
                public void onError(Exception e) {

                    Log.v("SettingActivity","连接失败！");
                }

                @Override
                public void onFinish() {
                }

            });
        }
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
        OnClick onClick = new OnClick();
        mBackBtn.setOnClickListener(onClick);
        mChangeintro.setOnClickListener(onClick);
        mChangephoto.setOnClickListener(onClick);
        mChangename.setOnClickListener(onClick);
        mChangepassword.setOnClickListener(onClick);
        mChangebirthday.setOnClickListener(onClick);
        mChangesex.setOnClickListener(onClick);
        mChangeaddress.setOnClickListener(onClick);
        mPreserve.setOnClickListener(onClick);
        My_pic.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            TextView textView1 = (TextView) findViewById(R.id.tv_sex);
            TextView textView2 = (TextView) findViewById(R.id.tv_address);
            TextView textView3 = (TextView) findViewById(R.id.tv_birthday);
            Phone phoneObj = (Phone)getApplication();
            final String phone = phoneObj.getPhone();
            String path1 = "http://139.219.4.34/editgender/";
            String path2 = "http://139.219.4.34/editregion/";
            String path3 = "http://139.219.4.34/editbirthday/";
            Map<String, String> userParams1 = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams1.put("phone", phone);
            Map<String, String> userParams2 = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams2.put("phone", phone);
            Map<String, String> userParams3 = new HashMap<String, String>();//将数据放在map里，便于取出传递
            userParams3.put("phone", phone);

            Intent intent = null;
            switch (v.getId()){
                case R.id.back:
                    intent = new Intent(SettingActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changephoto:
                    intent = new Intent(SettingActivity.this,ChangePhotoActivity.class);
                    intent.putExtra("pic_url",photo);
                    startActivity(intent);
                    break;
                case R.id.changepassword:
                    intent = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                    //界面跳转并传递参数password
                    intent.putExtra("password", password);
                    startActivity(intent);
                    break;
                case R.id.changename:
                    intent = new Intent(SettingActivity.this,ChangeNameActivity.class);
                    //界面跳转并传递参数nickname
                    intent.putExtra("nickname", nickname);
                    startActivity(intent);
                    break;
                case R.id.changeintro:
                    intent = new Intent(SettingActivity.this,ChangeIntroductionActivity.class);
                    //界面跳转并传递参数description
                    intent.putExtra("description", description);
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
                        Toast.makeText(SettingActivity.this, "请等待数据解析完成", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_preserve:
                    String birthday = textView3.getText().toString();
                    userParams3.put("birthday", birthday);
                    Log.v("SettingActivity",birthday);
                    HttpServer.SuperHttpUtil.post(userParams3,path3, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json=new JSONObject(result);
                            String change=result_json.getString("msg");
                            if(change.equals("success")){
                                Toast.makeText(SettingActivity.this,"生日修改成功", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(SettingActivity.this,"修改生日发生未知错误 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(SettingActivity.this,"修改生日服务器连接失败",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFinish() {
                        }
                    });
                    String gender = textView1.getText().toString();
                    userParams1.put("gender", gender);
                    Log.v("SettingActivity",gender);
                    HttpServer.SuperHttpUtil.post(userParams1,path1, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json=new JSONObject(result);
                            String change=result_json.getString("msg");
                            if(change.equals("success")){
                                Toast.makeText(SettingActivity.this,"修改性别成功", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(SettingActivity.this,"修改性别时发生未知错误 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(SettingActivity.this,"修改性别服务器连接失败",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFinish() {
                        }
                    });
                    String region = textView2.getText().toString();
                    userParams2.put("region", region);
                    Log.v("SettingActivity",region);
                    HttpServer.SuperHttpUtil.post(userParams2,path2, new HttpServer.SuperHttpUtil.HttpCallBack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            JSONObject result_json=new JSONObject(result);
                            String change=result_json.getString("msg");
                            if(change.equals("success")){
                                Toast.makeText(SettingActivity.this,"修改地区成功", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(SettingActivity.this,"修改地区时未知错误 ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(SettingActivity.this,"修改地区服务器连接失败",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFinish() {
                        }
                    });
                default:
                    break;

            }

        }
    }
}