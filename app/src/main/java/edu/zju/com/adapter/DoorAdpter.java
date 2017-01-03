package edu.zju.com.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import edu.zju.com.Fragment.DoorFragment;
import edu.zju.com.activity.ModifyDoorActivity;
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

    private DoorAdpter doorAdpter;

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

        mHolder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("xiaowen", "长按时间");

                final String[] listItems = {"修改", "删除"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);


                final String username = UserUtils.getUsername();//从上页获取用户名
                final String nameLocal = data.get(position).get("name");
                final String phy_addr_did = data.get(position).get("phy_addr_did");
                final String route = data.get(position).get("route");

                builder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listItems[which].equals("修改")) {
                            Log.i("xiaowen", "修改状态");

                            Intent intent = new Intent(context, ModifyDoorActivity.class);
                            intent.putExtra("name", nameLocal);
                            intent.putExtra("phy_addr_did", phy_addr_did);
                            intent.putExtra("route", route);
                            context.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteDoor(position, username, "door", nameLocal, phy_addr_did, route);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        return convertView;
    }

    private void deleteDoor(final int position, String username, String action, String nameLocal,
                            String phy_addr_did, String route) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("name", nameLocal);
        params.put("phy_addr_did", phy_addr_did);
        params.put("route", route);
        params.put("action", action);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "doorRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Log.i("xiaowen", "门删除成功");
                            Toast.makeText(context, "门删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "请求失败,请重新尝试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("xiaowen", "error--这是测试是否刷新");
                        Toast.makeText(context, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
                    }
                });
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
                                        .setTitleText("门状态改变")
                                        .show();
                            }
                        } else {
                            Toast.makeText(context, "请求失败,请重新尝试", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
