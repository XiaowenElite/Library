package edu.zju.com.activity;

import android.app.Activity;
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

public class AddLightActivity extends Activity implements OnClickListener {

    private EditText etLightName;
    private EditText etLightPhyAddr;
    private EditText etLightLine;

    private LinearLayout btnLightBack;
    private Button btnConfirmD;
    private Button btnUnConfirmD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlight);
        init();

    }

    private void init() {
        etLightName = (EditText) this.findViewById(R.id.et_lightName);
        etLightPhyAddr = (EditText) this.findViewById(R.id.et_lightPhyAddr);
        etLightLine = (EditText) this.findViewById(R.id.et_lightline);

        btnLightBack = (LinearLayout) this.findViewById(R.id.btn_lightBack);
        btnConfirmD = (Button) this.findViewById(R.id.saveLight);
        btnUnConfirmD = (Button) this.findViewById(R.id.unsaveLight);
        btnLightBack.setOnClickListener(this);
        btnConfirmD.setOnClickListener(this);
        btnUnConfirmD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveLight:
                String type = "light";
                String lightName = etLightName.getText().toString();
                String lightPhyAddr = etLightPhyAddr.getText().toString();
                String lightLine = etLightLine.getText().toString();
                if (!lightName.trim().equals("") && !lightPhyAddr.trim().equals("") && !lightLine.trim().equals("")) {
                    addLight(UserUtils.getUsername(), type, lightName, lightPhyAddr, lightLine);
                } else {
                    Toast.makeText(edu.zju.com.activity.AddLightActivity.this, "请填写全部信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.unsaveLight:
                finish();
                break;
            case R.id.btn_lightBack:
                UserUtils.setCurrentPage("1");
                finish();
                break;

            default:
                break;
        }
    }

    private void addLight(String params1, String params2, String params3, String params4, String params5) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", params1);
        params.put("type", params2);
        params.put("name", params3);
        params.put("phy_addr_did", params4);
        params.put("route", params5);
        String libid = UserUtils.getLibraryid();
        params.put("library_id",libid);

        String JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath() + "lightAdd")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.i("xiaowen", "result" + s);
                        //转换成Map
                        Map data = JsonUtil.fromJson(s, Map.class);
                        //get方法直接获取key对应的value
                        String result = (String) data.get("result");
                        Log.i("lbk", "result=" + data.get("result"));
                        if ("success".equals(result)) {
                            UserUtils.setCurrentPage("1");
                            new SweetAlertDialog(edu.zju.com.activity.AddLightActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("您已经成功添加灯")
                                    .setConfirmText("确认")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            UserUtils.setisRefreshLight("true");
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
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(edu.zju.com.activity.AddLightActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                        Log.i("xiaowen", "error");
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UserUtils.setCurrentPage("1");
        }
        return super.onKeyDown(keyCode, event);
    }
}
