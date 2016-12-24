package edu.zju.com.activity;

import com.igexin.sdk.PushManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        PushManager.getInstance().initialize(this.getApplicationContext());//初始化个推
        btnAdd = (Button) this.findViewById(R.id.btn_login);
        btnSign = (Button) this.findViewById(R.id.btn_sign);
        btnForget = (Button) this.findViewById(R.id.btn_forget);
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
                if (("".equals(name)&&"".equals(pwd))){
                    Toast.makeText(getApplicationContext(),"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if("".equals(name)){
                        Toast.makeText(getApplicationContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if("".equals(pwd)) {
                            Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            testpost(name,pwd);
                        }
                    }

                }



                break;
            case R.id.btn_sign:
                intent = new Intent(LoginActivity.this, SignActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget:
                intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void testpost(final String params1, final String params2){
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("password", params2);
        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"login")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.i("xiaowen","result"+s);
                        //转换成Map
                       Map data = JsonUtil.fromJson(s,Map.class);
                        //get方法直接获取key对应的value
                       String result = (String) data.get("result");
                        Log.i("lbk","result="+data.get("result"));
                        if("success".equals(result)) {


                            //传username
                            Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
                            //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                            Bundle bundle = new Bundle();
                            bundle.putString("username", params1);//将空调状态传给控制界面进行状态记录

                            //此处使用putExtras，接受方就响应的使用getExtras
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
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
