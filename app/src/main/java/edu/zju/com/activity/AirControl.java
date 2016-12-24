package edu.zju.com.activity;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
 * Created by bin on 2016/11/28.
 */

public class AirControl extends Activity implements View.OnClickListener{
    private TextView tvTem;
    private  TextView tvMode;
    private  TextView tvWinddir;
    private TextView tvWindSpeed;

    private String action = "";//ios用于辅助解析用，我这边没什么用
    //AirActivity传过来的值
    private String username;
    private String name;
    private String phy_addr_did;
    private String route;
    private String cmd;
    private Button btnKaiguan;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_control);
        init();
        //接收数据
        Intent intent = this.getIntent();
        Bundle bd = intent.getExtras();

        if (bd != null){
            username = bd.getString("username");
            name = bd.getString("name");
            phy_addr_did = bd.getString("phy_addr_did");
            route = bd.getString("route");
            cmd = bd.getString("cmd");
          if(cmd.equals("open")){
              btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_press);
          }
            else{
              btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_unpress);
          }

        }

    }
    private  void init(){
        tvTem =  (TextView) findViewById(R.id.tv_tem);
        tvMode = (TextView) findViewById(R.id.tv_mode);
        tvWinddir = (TextView) findViewById(R.id.tv_winddir);
        tvWindSpeed = (TextView) findViewById(R.id.tv_windspeed);

        Button btnBack = (Button) findViewById(R.id.btn_airControlBack);
        Button btnFensu = (Button) findViewById(R.id.btn_fengsu);
        Button btnFengxiang = (Button) findViewById(R.id.btn_fengxiang);
        btnKaiguan = (Button) findViewById(R.id.btn_kaiguan);
        Button btnMoshi = (Button) findViewById(R.id.btn_airMoshi);
        Button btnUp = (Button) findViewById(R.id.btn_temUp);
        Button btnDown = (Button) findViewById(R.id.btn_temDown);
        btnFensu.setOnClickListener(this);
        btnFengxiang.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnKaiguan.setOnClickListener(this);
        btnMoshi.setOnClickListener(this);
    }

    private void testpost(String params1, String params2, String params3,String params4,String params5,String params6,String params7,String params8,String params9) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("action", params2);
        params.put("name", params3);
        params.put("phy_addr_did", params4);
        params.put("route", params5);
        params.put("windspeed", params6);
        params.put("winddir", params7);
        params.put("temp", params8);
        params.put("mode", params9);

        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"airControl")//
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
                        if(result.equals("success")){
                            Log.i("lbk","空调操作成功");


                        }
                        else{
                            Toast.makeText(AirControl.this,"空调操作失败",Toast.LENGTH_SHORT).show();
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
    private void testpostcmd(String params1, String params2, String params3,String params4,String params5) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("name", params2);
        params.put("phy_addr_did", params3);
        params.put("route", params4);
        params.put("cmd", params5);

        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"airControl")//
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
                        if(result.equals("success")){
                            Log.i("lbk","空调操作成功");

                        }
                        else{
                            Toast.makeText(AirControl.this,"空调操作失败",Toast.LENGTH_SHORT).show();
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
    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        int windSpeed = 1;
        String winddir = tvWinddir.getText().toString();
        String temp = tvTem.getText().toString();
        Log.i("lbk",temp);
        int temprature = Integer.valueOf(temp);
        String mode = "xuehua";
        switch(view.getId()){

            case R.id.btn_airControlBack:

                //传username
                intent = new Intent(AirControl.this,AirActivity.class);
                //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                bundle = new Bundle();
                bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

                //此处使用putExtras，接受方就响应的使用getExtras
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_temUp:
                temprature = temprature+1;
                tvTem.setText(temprature+"");
                testpost(username,action,name,phy_addr_did,route,windSpeed+"",winddir,temprature+"",mode);
                break;

            case R.id.btn_kaiguan:
                if(cmd.equals("open")){
                    btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_unpress);
                    cmd = "close";
                }
                if(cmd.equals("close")){
                    btnKaiguan.setBackgroundResource(R.drawable.kaiguan_icon_press);
                    cmd = "open";
                }
                testpostcmd(username,name,phy_addr_did,route,cmd);
                break;
            case R.id.btn_airMoshi:
                testpost(username,action,name,phy_addr_did,route,windSpeed+"",winddir,temprature+"",mode);
                break;

            case R.id.btn_temDown:
                temprature = temprature-1;
                tvTem.setText(temprature+"");
                testpost(username,action,name,phy_addr_did,route,windSpeed+"",winddir,temprature+"",mode);
                break;

            case R.id.btn_fengsu:
                if(windSpeed==1){
                    windSpeed++;
                }
                if(windSpeed==3){
                    windSpeed--;
                }
                testpost(username,action,name,phy_addr_did,route,windSpeed+"",winddir,temprature+"",mode);
                break;

            case R.id.btn_fengxiang:

                testpost(username,action,name,phy_addr_did,route,windSpeed+"",winddir,temprature+"",mode);
                break;
            default:
                break;
        }
    }
}
