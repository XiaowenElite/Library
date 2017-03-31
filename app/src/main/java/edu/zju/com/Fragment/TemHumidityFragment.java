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

import edu.zju.com.adapter.TemHumAdapter;
import edu.zju.com.entity.TemHumBean;
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

public class TemHumidityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView = null;
    private Context context;

    private View view;
    private SwipeRefreshLayout mSwipeLayout;

    private List<TemHumBean.DataBean> list = null;

    public TemHumidityFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("xiaowen","onresume---temp)");
        if (UserUtils.getisRefreshTmep().equals("true")){
            getTemHumidity(false);
            UserUtils.setisRefreshTemp("false");
        }

    }

    public TemHumidityFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.temhumidity, null);
        listView = (ListView) view.findViewById(R.id.list_tem);
        init();
        return view;
    }

    @Override
    public void onRefresh() {

        getTemHumidity(true);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getTemHumidity(false);
        } else {
            OkGo.getInstance().cancelTag(this);
        }
    }

    private void init() {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_tem);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }

    private void getTemHumidity(final Boolean isRefresh) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", UserUtils.getUsername());
        params.put("type", "temphum");


        String libid = UserUtils.getLibraryid();
        params.put("library_id",libid);

        if (!isRefresh) {
            LoadingProgress.getInstance(context).show();
        }
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "synchrotemphum")
                .tag(this)
                .upJson(JsonString)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mSwipeLayout.setRefreshing(false);
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }

                        TemHumBean temHumBean = JsonUtil.fromJson(s, TemHumBean.class);
                        String count = temHumBean.getCount();
                        if (!count.equals("0")) {
                            list = temHumBean.getData();
                            listView.setAdapter(new TemHumAdapter(getActivity(), temHumBean.getData()));
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Toast.makeText(context, "温湿度同步失败", Toast.LENGTH_SHORT).show();
                        mSwipeLayout.setRefreshing(false);
                        OkGo.getInstance().cancelTag(this);
                    }
                });

    }
}
