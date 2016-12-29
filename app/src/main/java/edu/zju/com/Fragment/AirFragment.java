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

import edu.zju.com.adapter.AirMyAdpter;
import edu.zju.com.entity.AirBean;
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

public class AirFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private List<Map<String,String>> airInfoList;
    private View view;

    private SwipeRefreshLayout mSwipeLayout;

    private Context context;

    public AirFragment(){

    }

    public AirFragment (Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.air, null);
        context = view.getContext();
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        return view;
    }


    private void init() {
        listView = (ListView) view.findViewById(R.id.listair);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }


    @Override
    public void onStart() {
        super.onStart();
        init();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getAirs(false);//设备同步获取所有的空调
            //LoadingProgress.getInstance().show();
        }else {
            OkGo.getInstance().cancelTag(this);
        }
    }

    public void getAirs(final  Boolean isRefresh) {

        if (!isRefresh) {
            LoadingProgress.getInstance(context).show();
        }

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", UserUtils.getUsername());
        params.put("type", "air");

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "synchroair")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Toast.makeText(context, "空调同步成功", Toast.LENGTH_SHORT).show();

                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }

                        if (s != "" && s != null) {
                            AirBean airBean = JsonUtil.fromJson(s, AirBean.class);
                            airInfoList = airBean.getData();
                            if (airInfoList!=null) {
                                listView.setAdapter(new AirMyAdpter(context, airInfoList));
                            }
                        }
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Toast.makeText(context, "空调同步失败", Toast.LENGTH_SHORT).show();
                        OkGo.getInstance().cancelTag(this);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getAirs(true);
    }
}
