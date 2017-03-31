package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.adapter.DoorAdpter;
import edu.zju.com.adapter.RoomAdapter;
import edu.zju.com.entity.DataBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.LoadingProgress;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.utils.OkLogger.tag;

public class LibrarySumActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private ListView listView = null;
    private List<Map<String, String>> dataInfoList;//获取JSon数组相关

    private SwipeRefreshLayout mSwipeLayout;

    private LinearLayout btnBack;
    private LinearLayout btnAdd;

    private String number;//记录从服务器读取的设备数量,字符串
    private int count;//设备数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_sum);

        init();


        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Log.i("xiaowen", "屏幕尺寸2：宽度 = " + w_screen + "高度 = " + h_screen + "密度 = " + dm.densityDpi);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getrooms(false);
    }

    public void init() {
        listView = (ListView) findViewById(R.id.list);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_library_sum);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小


        btnBack = (LinearLayout) this.findViewById(R.id.btn_BackLibrary);
        btnAdd = (LinearLayout) this.findViewById(R.id.btn_addlibrary);

        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void getrooms(final Boolean isRefresh) {

        if (!isRefresh) {
            LoadingProgress.getInstance(this).show();
        }
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", UserUtils.getUsername());

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "synchrolibrary")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mSwipeLayout.setRefreshing(false);
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Map data = JsonUtil.fromJson(s, Map.class);
                        number = (String) data.get("count");
                        count = Integer.parseInt(number);//转化为整型
                        //读取Json数据的数组（设备信息）
                        if (count != 0) {
                            DataBean dataBean = JsonUtil.fromJson(s, DataBean.class);//拿到Json字符串S,用Gson直接解析成对象
                            dataInfoList = dataBean.getData();
                            listView.setAdapter(new RoomAdapter(LibrarySumActivity.this, dataInfoList));
                        }
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Toast.makeText(LibrarySumActivity.this, "房间同步失败", Toast.LENGTH_SHORT).show();
                        mSwipeLayout.setRefreshing(false);
                        OkGo.getInstance().cancelTag(this);
                    }
                });
    }


    @Override
    public void onRefresh() {
        getrooms(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_BackLibrary:
                finish();
                break;
            case R.id.btn_addlibrary:
                Intent intent = new Intent(LibrarySumActivity.this, AddRoomActivity.class);
                startActivity(intent);
                break;
        }
    }
}
