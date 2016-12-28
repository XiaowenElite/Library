package edu.zju.com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.LibraryApp;
import edu.zju.com.activity.ForgetPwdActivity;
import edu.zju.com.activity.LibraryActivity;
import edu.zju.com.activity.SignActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by bin on 2016/11/19.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button btnAdd;
    private Button btnSign;
    private Button btnForget;
    private EditText etName;
    private EditText etPwd;
    private Button eye;

    private Boolean isCansee = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();


        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.i("xiaowen",width+"");
        Log.i("xiaowen",height+"");

        if (UserUtils.getUsername() != null) {
            Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void init() {
        PushManager.getInstance().initialize(this.getApplicationContext());//初始化个推
        btnAdd = (Button) this.findViewById(R.id.btn_login);
        btnSign = (Button) this.findViewById(R.id.btn_sign);
        btnForget = (Button) this.findViewById(R.id.btn_forget);
        eye = (Button) this.findViewById(R.id.eye);
        eye.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSign.setOnClickListener(this);
        btnForget.setOnClickListener(this);
        etName = (EditText) this.findViewById(R.id.login_et_name);
        etPwd = (EditText) this.findViewById(R.id.login_et_pwd);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_login:
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();
                if (("".equals(name) && "".equals(pwd))) {
                    Toast.makeText(getApplicationContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if ("".equals(name)) {
                        Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else {
                        if ("".equals(pwd)) {
                            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                        } else {

                            login(name, pwd);
                        }
                    }

                }
                //testGet();
                break;
            case R.id.btn_sign:
                intent = new Intent(edu.zju.com.activity.LoginActivity.this, SignActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget:
                intent = new Intent(edu.zju.com.activity.LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.eye:
                if (isCansee == false) {
                    isCansee = true;
                    etPwd.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    isCansee = false;
                    etPwd.setTransformationMethod(null);
                }
            default:
                break;
        }

    }

    private void login(final String params1, final String params2) {


        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", params1);
        params.put("password", params2);
        String JsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath() + "login")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        pDialog.hide();
                        //上传成功
                        Log.i("xiaowen", "result" + s);
                        //转换成Map
                        Map data = JsonUtil.fromJson(s, Map.class);
                        //get方法直接获取key对应的value
                        String result = (String) data.get("result");
                        Log.i("lbk", "result=" + data.get("result"));
                        if ("success".equals(result)) {
                            UserUtils.setUsername(params1);
                            UserUtils.setPassword(params2);
                            //传username
                            Intent intent = new Intent(edu.zju.com.activity.LoginActivity.this, LibraryActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new SweetAlertDialog(edu.zju.com.activity.LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("用户名或者密码错误...")
                                    .show();
                        }

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        pDialog.hide();
                        new SweetAlertDialog(edu.zju.com.activity.LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("请重新尝试登录...")
                                .show();
                    }
                });
    }


}
