package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.entity.ResultEntity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/11/28.
 */

public class AirControl extends Activity implements View.OnClickListener {

    private TextView tvTem;
    private TextView tvMode;
    private TextView tvWinddir;
    private ImageView imgWindSpeed;

    private String username;
    private String name;
    private String phy_addr_did;
    private String route;
    private String cmd;


    private Button btnKaiguan;

    // 风速
    // 风向
    // 模式
    private String[] windspeeds = {"弱风", "强风"};
    private String[] windspeedsTo = {"1", "5"};
    private String[] winddirs = {"不扫风", "扫风"};
    private String[] winddirsTo = {"0", "1"};
    private String[] airmodes = {"制热", "制冷"};
    private String[] airmodesTo = {"2", "3"};


    private int countspeed = 0;
    private int countdir = 0;
    private int countmod = 0;


    String winddir = winddirsTo[0];
    String windSpeed = windspeedsTo[0];
    String mode = airmodesTo[1];
    String temp = "18";
    String action = "tempadd";

    int temprature = 0;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_control);
        init();
    }

    private void init() {

        tvTem = (TextView) findViewById(R.id.tv_tem);
        tvMode = (TextView) findViewById(R.id.tv_mode);
        tvWinddir = (TextView) findViewById(R.id.tv_winddir);
        imgWindSpeed = (ImageView) findViewById(R.id.im_fengsu);

        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        Button btnFensu = (Button) findViewById(R.id.fengsu);
        Button btnFengxiang = (Button) findViewById(R.id.fengxiang);
        btnKaiguan = (Button) findViewById(R.id.airswitch);
        Button btnMoshi = (Button) findViewById(R.id.moshi);
        Button btnUp = (Button) findViewById(R.id.tempUp);
        Button btnDown = (Button) findViewById(R.id.temdown);
        btnFensu.setOnClickListener(this);
        btnFengxiang.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnKaiguan.setOnClickListener(this);
        btnMoshi.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        username = UserUtils.getUsername();
        name = UserUtils.getAirname();
        phy_addr_did = UserUtils.getAiraddr();
        route = UserUtils.getAirroute();
        cmd = UserUtils.getAircmd();
        if (cmd.equals("open")) {
            btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_press);
        } else {
            btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_unpress);
        }
    }

    private void changeAirParam(String username, String action, String name, String addr, String route, String windspeed, String winddir, String mode, String temp) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", username);
        params.put("action", action);
        params.put("name", name);
        params.put("phy_addr_did", addr);
        params.put("route", route);
        params.put("windspeed", windspeed);
        params.put("winddir", winddir);
        params.put("temp", temp);
        params.put("mode", mode);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "airControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        ResultEntity resultEntity = JsonUtil.fromJson(s, ResultEntity.class);
                        String result = resultEntity.getResult();
                        if (result.equals("success")) {
                            Log.i("lbk", "空调操作成功");
                        } else {
                            Toast.makeText(edu.zju.com.activity.AirControl.this, "空调操作失败", Toast.LENGTH_SHORT).show();
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


    private void changeAirTemp(String username, String action, String name, String addr, String route, String windspeed, String winddir, String mode, final String temp) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", username);
        params.put("action", action);
        params.put("name", name);
        params.put("phy_addr_did", addr);
        params.put("route", route);
        params.put("windspeed", windspeed);
        params.put("winddir", winddir);
        params.put("temp", temp);
        params.put("mode", mode);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "airControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        ResultEntity resultEntity = JsonUtil.fromJson(s, ResultEntity.class);
                        String result = resultEntity.getResult();
                        if (result.equals("success")) {
                            tvTem.setText(temp);
                        } else {
                            Toast.makeText(edu.zju.com.activity.AirControl.this, "空调操作失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(edu.zju.com.activity.AirControl.this, "空调操作失败,请重新尝试", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void changeAirCmd(String username, String name, String addr, String route, String cmd) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", username);
        params.put("name", name);
        params.put("phy_addr_did", addr);
        params.put("route", route);
        params.put("cmd", cmd);

        String JsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath() + "airControl")//
                .tag(this)//
                .upJson(JsonString)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        ResultEntity resultEntity = JsonUtil.fromJson(s, ResultEntity.class);
                        String result = resultEntity.getResult();

                        if (result.equals("success")) {
                            Log.i("lbk", "空调操作成功");

                        } else {
                            Toast.makeText(edu.zju.com.activity.AirControl.this, "空调操作失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        new SweetAlertDialog(edu.zju.com.activity.AirControl.this)
                                .setTitleText("请重新请求")
                                .setContentText("It's pretty, isn't it?")
                                .show();
                    }
                });
    }


    @Override
    public void onClick(View view) {

        temprature = Integer.parseInt(tvTem.getText().toString().trim());

        switch (view.getId()) {
            case R.id.back:
                UserUtils.setCurrentPage("2");
                finish();
                break;
            case R.id.tempUp:
                if (temprature < 32) {
                    temprature = temprature + 1;
                    temp = temprature + "";
                    changeAirTemp(username, action, name, phy_addr_did, route, windSpeed, winddir, mode, temp);
                } else {
                    Toast.makeText(edu.zju.com.activity.AirControl.this, "已经是最高温度", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.airswitch:
                if (cmd.equals("open")) {
                    btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_unpress);
                    cmd = "close";
                } else {
                    btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_press);
                    cmd = "open";
                }
                changeAirCmd(username, name, phy_addr_did, route, cmd);
                break;
            case R.id.moshi:
                countmod++;
                int index = countmod % (airmodes.length);
                switch (index) {
                    case 1:
                        tvMode.setBackgroundResource(R.drawable.zhire);
                        mode = airmodesTo[0];
                        break;
                    case 0:
                        tvMode.setBackgroundResource(R.drawable.xuehua);
                        mode = airmodesTo[1];
                        break;
                }
                changeAirParam(username, action, name, phy_addr_did, route, windSpeed, winddir, mode, temp);
                break;

            case R.id.temdown:
                if (temprature > 18) {
                    temprature = temprature - 1;
                    temp = temprature + "";
                    changeAirTemp(username, action, name, phy_addr_did, route, windSpeed, winddir, mode, temp);
                } else {
                    Toast.makeText(edu.zju.com.activity.AirControl.this, "已经是最低温度", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.fengsu:
                countspeed++;
                index = countspeed % windspeeds.length;
                switch (index) {
                    case 0:
                        imgWindSpeed.setBackgroundResource(R.drawable.fengsu1ji);
                        windSpeed = windspeedsTo[0];
                        break;
                    case 1:
                        imgWindSpeed.setBackgroundResource(R.drawable.fengsu2ji);
                        windSpeed = windspeedsTo[1];
                        break;
                }
                changeAirParam(username, action, name, phy_addr_did, route, windSpeed, winddir, mode, temp);
                break;
            case R.id.fengxiang:
                countdir++;
                String winStr = "";
                index = countdir % winddirs.length;
                switch (index) {
                    case 0:
                        winStr = winddirs[0];
                        winddir = winddirsTo[0];
                        break;
                    case 1:
                        winStr = winddirs[1];
                        winddir = winddirsTo[1];
                        break;
                }
                tvWinddir.setText(winStr);
                changeAirParam(username, action, name, phy_addr_did, route, windSpeed, winddir, mode, temp);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UserUtils.setCurrentPage("2");
        }
        return super.onKeyDown(keyCode, event);
    }

}
