package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.LibraryApp;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.UserUtils;

/**
 * Created by bin on 2016/12/11.
 */

public class SetActivity extends Activity implements View.OnClickListener {

    TextView tvAddr;
    TextView tvPort;

    Button btnSetSave;
    LinearLayout btnSetBack;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlayout);
        LibraryApp.getInstance().addActivity(SetActivity.this);
        init();
    }

    private void init() {
        tvAddr = (TextView) findViewById(R.id.et_setAddr);
        tvPort = (TextView) findViewById(R.id.et_setPort);
        btnSetSave = (Button) findViewById(R.id.btn_setSave);
        btnSetBack = (LinearLayout) findViewById(R.id.btn_setBack);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        btnSetSave.setOnClickListener(this);
        btnSetBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setSave:
                String ipaddress = tvAddr.getText().toString().trim();
                String port = tvPort.getText().toString().trim();
                if (UserUtils.isIpAddress(ipaddress)) {
                    if (UserUtils.isPort(port)) {
                        //尝试请求 请求成功后在修改
                        HttpContant.setServerAddr(ipaddress);
                        HttpContant.setHttpPort(port);
                        new SweetAlertDialog(edu.zju.com.activity.SetActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("您已经成功修改地址和端口号")
                                .setConfirmText("确认")
                                .show();
                    } else {
                        new SweetAlertDialog(edu.zju.com.activity.SetActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("端口号格式错误")
                                .show();
                    }
                } else {
                    new SweetAlertDialog(edu.zju.com.activity.SetActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Ip地址格式错误")
                            .show();
                }
                break;
            case R.id.btn_setBack:
                finish();
                break;
            case R.id.btn_logout:
                UserUtils.setUsername(null);
                UserUtils.setPassword(null);
                Intent intent = new Intent(SetActivity.this,LoginActivity.class);
                startActivity(intent);
                LibraryApp.getInstance().exit();
            default:
                break;
        }
    }
}
