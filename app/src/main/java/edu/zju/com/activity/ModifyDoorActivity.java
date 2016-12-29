package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
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

public class ModifyDoorActivity extends Activity implements View.OnClickListener {

    private String name;
    private String addr;
    private String route;

    private TextView mf_name;
    private TextView mf_addr;
    private TextView mf_route;


    private String doorname;
    private String dooraddr;
    private String doorroute;

    private LinearLayout back;
    private Button modify;
    private Button cancle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifydoor);

        name = getIntent().getStringExtra("name");
        addr = getIntent().getStringExtra("phy_addr_did");
        route = getIntent().getStringExtra("route");

        init();
    }

    private void init() {
        mf_name = (TextView) findViewById(R.id.mf_doorName);
        mf_addr = (TextView) findViewById(R.id.mf_doorPhyAddr);
        mf_route = (TextView) findViewById(R.id.mf_doorLine);
        back = (LinearLayout) findViewById(R.id.mf_doorBack);
        modify = (Button) findViewById(R.id.door_modify);
        cancle = (Button) findViewById(R.id.door_cancle);


        mf_name.setText(name);
        mf_addr.setText(addr);
        mf_route.setText(route);

        back.setOnClickListener(this);
        modify.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.door_modify:
                modifyDoor();
                break;
            case R.id.door_cancle:
                finish();
                break;
            case R.id.mf_doorBack:
                finish();
                break;

        }
    }

    public void modifyDoor() {
        doorname = mf_name.getText().toString().trim();
        dooraddr = mf_addr.getText().toString().trim();
        doorroute = mf_route.getText().toString().trim();

        if (doorname.equals("") || dooraddr.equals("") || doorroute.equals("")) {
            Toast.makeText(ModifyDoorActivity.this, "请填写全部信息", Toast.LENGTH_SHORT).show();
        } else {
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", UserUtils.getUsername());
            params.put("name", name);
            params.put("phy_addr_did", addr);
            params.put("route", route);
            params.put("action", "door");
            params.put("name_edit", doorname);
            params.put("phy_addr_did_edit", dooraddr);
            params.put("route_edit", doorroute);
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
                                UserUtils.setCurrentPage("0");

                                new SweetAlertDialog(ModifyDoorActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText("您已经成功修改门")
                                        .setConfirmText("确认")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                Toast.makeText(ModifyDoorActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
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
