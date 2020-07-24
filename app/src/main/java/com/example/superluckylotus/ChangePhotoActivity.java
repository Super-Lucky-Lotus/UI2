package com.example.superluckylotus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version: 2.0
 * @author: 黄诗雯
 * @className: ChangePhotoActivity
 * @packageName:com.example.superluckylotus
 * @description: 更改头像界面
 * @data: 2020.07.14 16:38
 **/
public class ChangePhotoActivity extends AppCompatActivity {

    private String pic_url;
    private Boolean change = false;

    public static final int CHOOSE_PHOTO = 2;
    Button mSave;
    ImageButton mbacktosetting;
    MyImageView mImagePhoto;
    Button mOpenAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);
        mbacktosetting=(ImageButton)findViewById(R.id.backtosettting);
        mImagePhoto=(MyImageView)findViewById(R.id.iv_photo);
        mOpenAlbum=findViewById(R.id.btn_changephoto);
        mSave = (Button)findViewById(R.id.btn_save);

        getData();
        setListeners();
    }

    private void getData() {
        Intent intent = getIntent();
        pic_url = intent.getStringExtra("pic_url");
        mImagePhoto.setImageURL(pic_url);
    }

    private void setListeners(){
        ChangePhotoActivity.OnClick onClick = new ChangePhotoActivity.OnClick();
        mbacktosetting.setOnClickListener(onClick);
        mOpenAlbum.setOnClickListener(onClick);
        mSave.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.backtosettting:
                    intent = new Intent(ChangePhotoActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_changephoto:
                    //调用打开相册
                    getPhotoFromAlbum();
                    break;
                case R.id.btn_save:
                    if (change) {
                        Phone phoneObj = (Phone) getApplication();
                        final String phone = phoneObj.getPhone();
                        Log.v("ChangePhotoActivity", phone);
                        String path = "http://139.219.4.34/editimage/";
                        Map<String, String> userParams = new HashMap<String, String>();//将数据放在map里，便于取出传递
                        userParams.put("phone", phone);
                        Map<String, String> photoParams = new HashMap<>();
                        photoParams.put("face_image", pic_url);


                        HttpServer.SuperHttpUtil.post(userParams, path, new HttpServer.SuperHttpUtil.HttpCallBack() {
                            @Override
                            public void onSuccess(String result) throws JSONException {
                                JSONObject result_json = new JSONObject(result);
                                String login = result_json.getString("msg");
                                if (login.equals("success")) {
                                    Toast.makeText(ChangePhotoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = null;
                                    intent = new Intent(ChangePhotoActivity.this, SettingActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ChangePhotoActivity.this, "未知错误 ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ChangePhotoActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinish() {
                            }
                        });
                    }
                    else{
                        Intent intent2 = null;
                        intent2 = new Intent(ChangePhotoActivity.this, SettingActivity.class);
                        startActivity(intent2);
                    }
                    break;
            }
        }
    }

    //判断是否有权限
    private void getPhotoFromAlbum() {
        if (ContextCompat.checkSelfPermission(ChangePhotoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChangePhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }
    //检测是否有对应权限，没有的话申请权限，用户确认或拒绝后，系统自动调用onRequestPermissionsResult()方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openAlbum();
        } else {
            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    //使用Intent打开相册，用户选择照片后，系统调用onActivityResult()
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            handleImageOnKitKat(data);
        }
    }


    //处理Intent返回的数据，转化为路径，然后显示
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        change = true;
        // 根据图片路径显示图片
        pic_url = imagePath;
        displayImage(imagePath);
    }

//获取图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmap2; //裁剪生成的新图片的bitmap值
            int x=10; //从图片的x轴的x处开始裁剪
            int y=10; //从图片的y轴的y处开始裁剪
            int image_width=400; //裁剪生成新图皮的宽
            int image_height=400; //裁剪生成新图皮的高
            //获取图片bitmap值
            bitmap2 = Bitmap.createBitmap(bitmap, x, y, image_width, image_height);
            mImagePhoto.setImageBitmap(bitmap2);
        }
        else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


}