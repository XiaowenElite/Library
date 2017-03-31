package edu.zju.com.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
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

public class ModifyLibrary extends Activity implements View.OnClickListener{
    private String name;
    private String id;

    private TextView mf_libraryName;

    private LinearLayout back;
    private Button modify;
    private Button cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_library);

        name = getIntent().getStringExtra("library");
        id = getIntent().getStringExtra("library_id");

        init();
    }

    private void init() {
        mf_libraryName = (TextView) findViewById(R.id.mf_libraryName);

        back = (LinearLayout)findViewById(R.id.mf_roomBack);
        modify = (Button)findViewById(R.id.library_modify);
        cancle = (Button)findViewById(R.id.library_cancle);

        mf_libraryName.setText(name);

        back.setOnClickListener(this);
        modify.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.library_cancle:
                break;
            case R.id.library_modify:
                modifyLibrary();
                finish();
                break;
            case R.id.mf_roomBack:
                finish();
                break;
        }
    }

    public void modifyLibrary() {
        String libraryname = mf_libraryName.getText().toString().trim();

        if (libraryname.equals("")) {
            Toast.makeText(ModifyLibrary.this, "请填写全部信息", Toast.LENGTH_SHORT).show();
        } else {
            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", UserUtils.getUsername());
            params.put("library_edit", libraryname);
            params.put("library_id",id);
            params.put("action", "library");


            String JsonString = JsonUtil.toJson(params);

            OkGo.post(HttpContant.getUnencryptionPath() + "libraryEdit")//
                    .tag(this)//
                    .upJson(JsonString)//
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                            String result = resultEntity.get("result");

                            if (result.equals("success")) {
                                UserUtils.setCurrentPage("1");

                                new SweetAlertDialog(ModifyLibrary.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                Toast.makeText(ModifyLibrary.this,result.toString(), Toast.LENGTH_SHORT).show();
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
