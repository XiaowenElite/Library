package edu.zju.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/11/20.
 */

public class DoorAdpter extends BaseAdapter {
    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    ViewHolder mHolder;

    public DoorAdpter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }


    public final class ViewHolder {
        public TextView name;
        public ToggleButton onOff;
    }


    @Override
    public int getCount() {
        return data.size();
    }
    /*
    * 获得某一位置的数据
    * */

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /*
    * 获得唯一标识
    * */

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mHolder = new ViewHolder();
        if (convertView == null) {
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.listdoorlayout, null);
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_doorname);
            mHolder.onOff = (ToggleButton) convertView.findViewById(R.id.tg_onOff);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
//        绑定数据
        mHolder.name.setText(data.get(position).get("name"));
        mHolder.onOff.setBackgroundResource(R.drawable.ios7_btn);
        //门的状态
        String status = data.get(position).get("cmd");
        if (status != null) {
            if (status.equals("open")) {
                mHolder.onOff.setChecked(true);
            } else if (status.equals("close")) {
                mHolder.onOff.setChecked(false);
            }
        } else {
            Log.i("xiaowen", "门同步失败");
            mHolder.onOff.setChecked(false);
        }

        mHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            String username = UserUtils.getUsername();
            String nameLocal = data.get(position).get("name");
            String phy_addr_did = data.get(position).get("phy_addr_did");
            String route = data.get(position).get("route");

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    String cmd = "open";
                    changeState(username, nameLocal, phy_addr_did, route, cmd);
                } else {
                    String cmd = "close";
                    changeState(username, nameLocal, phy_addr_did, route, cmd);
                }
            }
        });
        return convertView;
    }

    private void changeState(String params1, String params2, String params3, String params4, String params5) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("name", params2);
        params.put("phy_addr_did", params3);
        params.put("route", params4);
        params.put("cmd", params5);

        String JsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath() + "doorControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Map data = JsonUtil.fromJson(s, Map.class);
                        if (data != null) {
                            String result = (String) data.get("result");
                            if (result.equals("success")) {
                                new SweetAlertDialog(context)
                                        .setTitleText("修改成功")
                                        .show();
                            }
                        } else {
                            Toast.makeText(context, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
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
