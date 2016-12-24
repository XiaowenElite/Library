package edu.zju.com.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.adapter.DoorAdpter;
import edu.zju.com.entity.DataBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.LoadingProgress;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lixiaowen on 16/12/13.
 */

public class DoorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView = null;
    private String number;//记录从服务器读取的设备数量,字符串
    private int count;//设备数量
    private List<Map<String, String>> dataInfoList;//获取JSon数组相关
    private SwipeRefreshLayout mSwipeLayout;
    private View view;
    private Context context;


    public DoorFragment(){

    }

    public DoorFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.door, null);
        listView = (ListView) view.findViewById(R.id.list);
        init();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        //getdoors();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getdoors(false);
        } else {
            OkGo.getInstance().cancelTag(this);
        }
    }

    public void init() {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }

    public void getdoors(final Boolean isRefresh) {

        if (!isRefresh) {
            LoadingProgress.getInstance(context).show();
        }
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", UserUtils.getUsername());
        params.put("type", "door");

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "synchrodoor")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Toast.makeText(context, "门同步成功", Toast.LENGTH_SHORT).show();


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
                            listView.setAdapter(new DoorAdpter(getActivity(), dataInfoList));
                            mSwipeLayout.setRefreshing(false);
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
                        Toast.makeText(context, "门同步失败", Toast.LENGTH_SHORT).show();
                        mSwipeLayout.setRefreshing(false);
                        OkGo.getInstance().cancelTag(this);
                    }
                });
    }


    @Override
    public void onRefresh() {
        getdoors(true);
    }
}
