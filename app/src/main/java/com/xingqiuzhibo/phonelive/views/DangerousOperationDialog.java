package com.xingqiuzhibo.phonelive.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;


/**
 * Created by hx
 * Time 2018/10/17/017.
 * 自定义危险操作对话框
 */

public class DangerousOperationDialog extends Dialog {

    private Button positiveButton, negativeButton;
    private TextView contentText;

    public DangerousOperationDialog(Context context) {
        super(context, R.style.mydialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_dangerous_operation, null);  //通过LayoutInflater获取布局
        contentText = view.findViewById(R.id.tv_dialog_title);
        positiveButton = view.findViewById(R.id.btn_dialog_positive);
        negativeButton = view.findViewById(R.id.btn_dialog_negative);
        setContentView(view);  //设置view
    }

    //设置内容
    public void setContent(String content) {
        if (null != content)
            contentText.setText(content);
    }

    //设置确认按钮文字
    public void setPositiveText(String positiveText) {
        if (null != positiveText)
            positiveButton.setText(positiveText);
    }

    //确定按钮监听
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    //否定按钮监听
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}
