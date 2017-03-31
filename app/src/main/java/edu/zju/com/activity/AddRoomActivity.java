package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.activity.LibraryActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;


public class AddRoomActivity extends Activity {

    private Button btnConfirm;
    private Button btnUnConfirm;
    private Button btnBack;

    private EditText libName;
    private EditText addName;

    private String name;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addroom);
        libName = (EditText) this.findViewById(R.id.et_libName);
        addName = (EditText) this.findViewById(R.id.et_addName);
        btnConfirm = (Button) this.findViewById(R.id.btn_confirm);
        btnUnConfirm = (Button) this.findViewById(R.id.btn_unconfirm);
        btnBack = (Button) this.findViewById(R.id.btn_addRoomBack);

        btnConfirm.setOnClickListener(listener);
        btnUnConfirm.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);

    }

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    name = libName.getText().toString();
                    address = addName.getText().toString();

                    if (!name.trim().equals("")&&!address.trim().equals("")) {
                        addLibrary(UserUtils.getUsername(), name, address);
                    }else {
                        Toast.makeText(AddRoomActivity.this,"请填写全部信息",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_unconfirm:
                    Intent intent2 = new Intent();
                    intent2.setClass(AddRoomActivity.this, LibrarySumActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btn_addRoomBack:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void addLibrary(String params1,String params2,String params3){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username",params1);
        params.put("library",params2);
        params.put("phy_addr_gid", params3);
        String  JsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath()+"libraryAdd")//
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
                        if("success".equals(result)) {
                            new SweetAlertDialog(AddRoomActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("您已经成功添加房间")
                                    .setConfirmText("确认")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),data.get("result")+"",Toast.LENGTH_SHORT).show();
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
