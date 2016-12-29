package edu.zju.com.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import edu.zju.com.adapter.LightSenseAdapter;
import edu.zju.com.entity.LightSenseBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.LoadingProgress;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lixiaowen on 16/12/28.
 */

public class LightSenceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView = null;
    private View view;
    private Context mcontext;
    private SwipeRefreshLayout mSwipeLayout;


    private List<LightSenseBean.DataBean> dataInfoList;

    public LightSenceFragment(Context context) {
        mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lightsense, null);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_lightSense);
        listView = (ListView) view.findViewById(R.id.list_lightsense);
        init();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getlightSense(false);
        } else {
            OkGo.getInstance().cancelTag(this);
        }
    }


    private void init() {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_lightSense);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }

    public void getlightSense(final Boolean isRefresh) {

        if (!isRefresh) {
            LoadingProgress.getInstance(mcontext).show();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("username", UserUtils.getUsername());
        params.put("type", "lightsense");

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "synchrolightsense")
                .tag(this)
                .upJson(JsonString)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        LightSenseBean data = JsonUtil.fromJson(s, LightSenseBean.class);
                        String count = data.getCount();
                        if (!count.equals("0")) {
                            dataInfoList = data.getData();
                            listView.setAdapter(new LightSenseAdapter(getActivity(), dataInfoList));
                        }

                        mSwipeLayout.setRefreshing(false);

                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {

                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Toast.makeText(mcontext, "光感同步失败", Toast.LENGTH_SHORT).show();
                        OkGo.getInstance().cancelTag(this);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
    }


    @Override
    public void onRefresh() {
        getlightSense(true);
    }
}
