package com.example.superluckylotus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CodeLoginActivity extends AppCompatActivity {
    Button mBacktoLogin;
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        mBacktoLogin=findViewById(R.id.backtologin2);
        mLogin=findViewById(R.id.btn_login2);
        setListeners(this);
    }

    private void setListeners(Context context){
        CodeLoginActivity.OnClick onClick = new OnClick();
        mLogin.setOnClickListener(onClick);
        mBacktoLogin.setOnClickListener(onClick);

    }
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.backtologin2:
                    intent = new Intent(CodeLoginActivity.this,LoginActivity.class);
                    break;
                case R.id.btn_login2://MainActivity要改成reg的act
                    intent = new Intent(CodeLoginActivity.this,MainActivity.class);
                    break;

            }
            startActivity(intent);


        }
    }

}