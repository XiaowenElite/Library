package edu.zju.com.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

public class SignActivity extends Activity implements OnClickListener {


    private EditText etName;
    private EditText etPwd;
    private EditText etEmail;
    private EditText etTel;
    private EditText etGateWay;
    private EditText etRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_main);
        init();

    }

    private void init() {
        Button btn_add = (Button) this.findViewById(R.id.btn_signUp);
        LinearLayout btnBack = (LinearLayout) this.findViewById(R.id.btn_signBack);
        etName = (EditText) this.findViewById(R.id.sign_et_name);
        etPwd = (EditText) this.findViewById(R.id.sign_et_pwd);
        etEmail = (EditText) this.findViewById(R.id.sign_et_email);
        etTel = (EditText) this.findViewById(R.id.sign_et_tel);
        etGateWay = (EditText) this.findViewById(R.id.sign_et_gateway);
        etRoomName = (EditText) this.findViewById(R.id.sign_et_roomname);

        btn_add.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void signUser(final String username, String password, String email, String cellphone, String addr,String roomname) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("cellphone", cellphone);
        params.put("phy_addr", addr);//会给固定物理地址00-01-6C-06-A6-29
        params.put("palce",roomname);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "sign")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        pDialog.hide();
                        Map data = JsonUtil.fromJson(s, Map.class);
                        String result = (String) data.get("result");
                        if ("success".equals(result)) {
                            UserUtils.setUsername(username);
                            new SweetAlertDialog(edu.zju.com.activity.SignActivity.this)
                                    .setTitleText("请重新登录")
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(), data.get("result") + "", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        pDialog.hide();
                        new SweetAlertDialog(edu.zju.com.activity.SignActivity.this)
                                .setTitleText("Error...")
                                .setContentText("网络连接超时")
                                .setConfirmText("确认")
                                .show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_signUp:
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();
                String email = etEmail.getText().toString();
                String tel = etTel.getText().toString();
                String gateway = etGateWay.getText().toString();
                String roomname = etRoomName.getText().toString();

                if (("".equals(name) && "".equals(pwd) && "".equals(email) && "".equals(tel) && "".equals(gateway))) {
                    Toast.makeText(getApplicationContext(), "请输入注册信息", Toast.LENGTH_SHORT).show();
                } else {
                    if ("".equals(name)) {
                        Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(pwd)) {
                            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if ("".equals((email))) {
                                Toast.makeText(getApplicationContext(), "邮箱不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                if ("".equals((tel))) {
                                    Toast.makeText(getApplicationContext(), "电话不能为空", Toast.LENGTH_SHORT).show();
                                } else {
                                    if ("".equals((gateway))) {
                                        Toast.makeText(getApplicationContext(), "网关不能为空", Toast.LENGTH_SHORT).show();
                                    } if("".equals(roomname)){
                                        Toast.makeText(getApplicationContext(), "图书馆名称不能为空", Toast.LENGTH_SHORT).show();

                                    }else {

                                        signUser(name, pwd, email, tel, gateway,roomname);
                                    }
                                }

                            }

                        }
                    }

                }
                break;
            case R.id.btn_signBack:
                finish();
                break;
            default:
                break;
        }
    }

}
