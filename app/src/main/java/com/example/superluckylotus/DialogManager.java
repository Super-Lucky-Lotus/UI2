package com.example.superluckylotus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class DialogManager {
    private Context mContext;
    private AlertDialog.Builder builder;

    public DialogManager(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    /**
     * 日期对话框
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dateDialog() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker dp, int year, int month,
                                          int dayOfMonth) {
                        showToast(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    private void showToast(String msg) {
        Toast.makeText(mContext, msg, 0).show();
    }


}
