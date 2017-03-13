package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class AddAirActivity extends Activity implements OnClickListener {

    private EditText etAirName;
    private EditText etAirPhyAddr;
    private EditText etAirLine;
    private EditText etPowerAddr;
    private EditText etPowerLine;

    private LinearLayout btnAirBack;
    private Button btnConfirmD;
    private Button btnUnConfirmD;

    private String username;//登录用户名


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addair);

        username = UserUtils.getUsername();

        init();

    }

    private void init() {
        etAirName = (EditText) this.findViewById(R.id.et_airName);
        etAirPhyAddr = (EditText) this.findViewById(R.id.et_airPhyAddr);
        etAirLine = (EditText) this.findViewById(R.id.et_airline);
        etPowerAddr = (EditText)findViewById(R.id.et_poweraddr);
        etPowerLine = (EditText)findViewById(R.id.et_powerline);

        btnAirBack = (LinearLayout) this.findViewById(R.id.btn_airBack);
        btnConfirmD = (Button) this.findViewById(R.id.saveAir);
        btnUnConfirmD = (Button) this.findViewById(R.id.unsaveAir);
        btnAirBack.setOnClickListener(this);
        btnConfirmD.setOnClickListener(this);
        btnUnConfirmD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.saveAir:
                String type = "air";
                String airName = etAirName.getText().toString();
                String airPhyAddr = etAirPhyAddr.getText().toString();
                String airLine = etAirLine.getText().toString();
                String powerAddr = etPowerAddr.getText().toString();
                String powerLine = etPowerLine.getText().toString();

                if (!airName.trim().equals("") && !airPhyAddr.trim().equals("") && !airLine.trim().equals("")
                        &&!powerAddr.trim().equals("")&&!powerLine.trim().equals("")) {
                    addAirs(username, type, airName, airPhyAddr, airLine,powerAddr,powerLine);
                } else {
                    Toast.makeText(AddAirActivity.this, "请填写全部信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.unsaveAir:
                finish();
                break;
            case R.id.btn_airBack:
                UserUtils.setCurrentPage("2");
                finish();
                break;

            default:
                break;
        }
    }

    private void addAirs(String params1, String params2, String params3, String params4,
                         String params5,String powerAddr,String powerLine) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", params1);
        params.put("type", params2);
        params.put("name", params3);
        params.put("phy_addr_did", params4);
        params.put("route", params5);
        params.put("pow_addr_did",powerAddr);
        params.put("pow_route",powerLine);
        String JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath() + "airAdd")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //转换成Map
                        Map data = JsonUtil.fromJson(s, Map.class);
                        //get方法直接获取key对应的value
                        String result = (String) data.get("result");
                        if ("success".equals(result)) {
                            UserUtils.setCurrentPage("2");
                            new SweetAlertDialog(AddAirActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("您已经成功添加空调")
                                    .setConfirmText("确认")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(), "保存失败:"+result.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("xiaowen", "error");
                        Toast.makeText(AddAirActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UserUtils.setCurrentPage("2");
        }
        return super.onKeyDown(keyCode, event);
    }
}
