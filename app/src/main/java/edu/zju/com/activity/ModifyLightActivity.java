package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

/**
 * Created by lixiaowen on 16/12/28.
 */

public class ModifyLightActivity extends Activity implements View.OnClickListener {

    private String name;
    private String addr;
    private String route;

    private TextView mf_name;
    private TextView mf_addr;
    private TextView mf_route;


    private String lightname;
    private String lightaddr;
    private String lightroute;

    private LinearLayout back;
    private Button modify;
    private Button cancle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifylight);

        name = getIntent().getStringExtra("name");
        addr = getIntent().getStringExtra("phy_addr_did");
        route = getIntent().getStringExtra("route");

        init();
    }

    private void init() {
        mf_name = (TextView) findViewById(R.id.mf_lightName);
        mf_addr = (TextView) findViewById(R.id.mf_lightPhyAddr);
        mf_route = (TextView) findViewById(R.id.mf_lightLine);

        back = (LinearLayout)findViewById(R.id.mf_lightBack);
        modify = (Button)findViewById(R.id.light_modify);
        cancle = (Button)findViewById(R.id.light_cancle);

        back.setOnClickListener(this);
        modify.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light_modify:
                modifyLight();
                break;
            case R.id.light_cancle:
                finish();
                break;
            case R.id.mf_lightBack:
                finish();
                break;
        }
    }

    public void modifyLight() {
        lightname = mf_name.getText().toString().trim();
        lightaddr = mf_addr.getText().toString().trim();
        lightroute= mf_route.getText().toString().trim();

        if (lightname.equals("") || lightaddr.equals("") || lightroute.equals("")) {
            Toast.makeText(ModifyLightActivity.this, "请填写全部信息", Toast.LENGTH_SHORT).show();
        } else {
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", UserUtils.getUsername());
            params.put("name", name);
            params.put("phy_addr_did", addr);
            params.put("route", route);
            params.put("action", "light");
            params.put("name_edit", lightname);
            params.put("phy_addr_did_edit", lightaddr);
            params.put("route_edit", lightroute);
            String JsonString = JsonUtil.toJson(params);

            OkGo.post(HttpContant.getUnencryptionPath() + "doorEdit")//
                    .tag(this)//
                    .upJson(JsonString)//
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                            String result = resultEntity.get("result");

                            if (result.equals("success")) {
                                new SweetAlertDialog(ModifyLightActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("您已经成功修改灯")
                                        .setConfirmText("确认")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                Toast.makeText(ModifyLightActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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
                        }
                    });
        }
    }
}
