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

import edu.zju.com.adapter.LightMyAdpter;
import edu.zju.com.entity.LightBean;
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

public class LigthFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;

    String number;
    int count;
    List<Map<String,String>> lightInfoList;
    private ListView listView= null;
    View view;
    private Context context;

    public LigthFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.light, null);
        listView = (ListView) view.findViewById(R.id.listlight);
        init();
        return view;
    }


    public void init(){
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }

    @Override
    public void onResume() {
        Log.i("xiaowen","onresume---light)");
        super.onResume();
        if (UserUtils.getisRefreshLight().equals("true")){
            getLights(false);
            UserUtils.setisRefreshLight("false");
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getLights(false);//设备同步获取所有的灯
           // LoadingProgress.getInstance(getContext()).show();
        }else {
            OkGo.getInstance().cancelTag(this);
        }
    }

    public void getLights(final Boolean isRefresh){
        HashMap<String, String> params = new HashMap<String, String>();
        if (!isRefresh) {
            LoadingProgress.getInstance(context).show();
        }


        String libraryname = UserUtils.getLibraryname();


        params.put("username",UserUtils.getUsername());
        params.put("type", "light");
        String libid = UserUtils.getLibraryid();
        params.put("library_id",libid);

        String  JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath()+"synchrolight")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Toast.makeText(context, "灯同步成功", Toast.LENGTH_SHORT).show();

                        if (!isRefresh) {
                            LoadingProgress.getInstance().dismiss();
                        }
                        Map data = JsonUtil.fromJson(s,Map.class);

                        number = (String) data.get("count");
                        count = Integer.parseInt(number);//转化为整型
                        if(count!= 0) {
                            LightBean lightBean = JsonUtil.fromJson(s, LightBean.class);//拿到Json字符串S,用Gson直接解析成对象
                            lightInfoList = lightBean.getData();
                            listView.setAdapter(new LightMyAdpter(getActivity(), lightInfoList));
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
                        Toast.makeText(context, "灯同步失败", Toast.LENGTH_SHORT).show();
                        OkGo.getInstance().cancelTag(this);
                        mSwipeLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        getLights(true);
    }
}
