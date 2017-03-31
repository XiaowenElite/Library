package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.LibraryApp;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

public class LibraryActivity extends Activity implements OnClickListener {

    private LinearLayout btnRoom;
    private Button btnScene;
    private Button btnSet;
    private Button btnHelp;
    private Button btnTem;
    private TextView txroom;
    private Button btnsafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);
        LibraryApp.getInstance().addActivity(LibraryActivity.this);
        init();
    }

    private void init() {
        btnRoom = (LinearLayout) this.findViewById(R.id.btn_room);
        btnScene = (Button) this.findViewById(R.id.btn_scene);
        btnSet = (Button) this.findViewById(R.id.btn_set);
        btnHelp = (Button) this.findViewById(R.id.btn_help);
        btnTem = (Button) findViewById(R.id.btn_tem);
        txroom = (TextView) findViewById(R.id.tx_room);

        btnRoom.setOnClickListener(this);
        btnScene.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnTem.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> params = new HashMap<>();
        params.put("username", UserUtils.getUsername());
        String jsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath() + "synchroplace")
                .tag(this)
                .upJson(jsonString)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.i("xiaowen", "result" + s);
                        //转换成Map
                        Map data = JsonUtil.fromJson(s, Map.class);
                        //get方法直接获取key对应的value
                        txroom.setText(data.get("place").toString());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        UserUtils.setCurrentPage("0");
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_room:
                //传username
                intent = new Intent(LibraryActivity.this, LibrarySumActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_scene:
                Toast.makeText(this, "敬请期待更多功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set:
                intent = new Intent();
                intent.setClass(edu.zju.com.activity.LibraryActivity.this, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_help:
                intent = new Intent();
                intent.setClass(edu.zju.com.activity.LibraryActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tem:
                intent = new Intent(LibraryActivity.this, EnviormentActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
