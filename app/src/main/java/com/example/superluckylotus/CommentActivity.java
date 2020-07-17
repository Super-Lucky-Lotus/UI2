package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @version: 2.0
 * @author: 宋佳容
 * @className: NewFansActivity
 * @packageName:com.example.superluckylotus
 * @description: 查看评论列表界面
 * @data: 2020.07.14 16:45
 **/

public class CommentActivity extends AppCompatActivity {

    private Button back_Btn;
    private Button comment_video1;
    private Button comment_video2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back_Btn = (Button) findViewById(R.id.cm_back);
        comment_video1 = (Button) findViewById(R.id.commentVedio1_btn);
        comment_video2 = (Button) findViewById(R.id.commentVedio2_btn);
        setListeners();
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        back_Btn.setOnClickListener(onClick);
        comment_video1.setOnClickListener(onClick);
        comment_video2.setOnClickListener(onClick);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.cm_back:
                    finish();
                    break;
                case R.id.commentVedio1_btn:
                case R.id.commentVedio2_btn:
                    intent = new Intent(CommentActivity.this, SingleVideoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}