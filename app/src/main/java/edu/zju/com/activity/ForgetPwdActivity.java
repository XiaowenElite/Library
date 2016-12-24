package edu.zju.com.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.activity.LoginActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/12/8.
 */

public class ForgetPwdActivity extends Activity implements View.OnClickListener {
    private EditText etEmail;
    private Button btnForgetPwd;
    private LinearLayout btnForgetPwdBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpwd);
        init();
    }
private void init(){
    etEmail = (EditText) this.findViewById(R.id.forget_et_email);
    btnForgetPwd = (Button) this.findViewById(R.id.btn_forgetPwd);
    btnForgetPwdBack = (LinearLayout) this.findViewById(R.id.btn_forgetPwdBack);
    btnForgetPwd.setOnClickListener(this);
    btnForgetPwdBack.setOnClickListener(this);
}
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forgetPwd:
                String eMail = etEmail.getText().toString();
                if("".equals(eMail)){
                    Toast.makeText(getApplicationContext(),"输入邮箱不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    testpost(eMail);
                }
                break;
            case R.id.btn_forgetPwdBack:
                finish();
                break;
            default:
                break;
        }
    }
    private void testpost(String params1){
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("email", params1);

        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"forgetpwd")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("xiaowen","result"+s);
                        Map data = JsonUtil.fromJson(s,Map.class);
                        String result = (String) data.get("result");
                        Log.i("lbk","result="+data.get("result"));
                        if("密码已发送到邮箱！".equals(result)) {
                            new SweetAlertDialog(edu.zju.com.activity.ForgetPwdActivity.this)
                                    .setTitleText("密码已经发送到邮箱")
                                    .show();
                            Intent intent = new Intent(edu.zju.com.activity.ForgetPwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"密码找回失败！",Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("xiaowen","error");
                    }
                });
    }
}
