package edu.zju.com.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


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
import edu.zju.com.activity.AirControl;
import edu.zju.com.activity.ModifyAirActivity;
import edu.zju.com.entity.ResultEntity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/11/20.
 */

public class AirMyAdpter extends BaseAdapter {
    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context mContext;
    ViewHolder mHolder;
    String power = "close";


    public AirMyAdpter(Context context, List<Map<String, String>> data) {
        this.mContext = context;
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
            convertView = layoutInflater.inflate(R.layout.listlayoutair, null);//对应list布局文件
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_airname);
            mHolder.onOff = (ToggleButton) convertView.findViewById(R.id.tg_onOff);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
//        绑定数据
        mHolder.name.setText(data.get(position).get("name"));
        mHolder.onOff.setBackgroundResource(R.drawable.ios7_btn);
        //电源状态
        power = data.get(position).get("power");
        if (power != null) {
            if (power.equals("open")) {
                mHolder.onOff.setChecked(true);
            } else if (power.equals("close")) {
                Log.i("xiaowen", "空调电源同步失败");
                mHolder.onOff.setChecked(false);
            }

        } else {
            power = "false";
            mHolder.onOff.setChecked(false);
        }
        mHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (power.equals("open")) {
                    //取出当前数组数据
                    UserUtils.setAirname(data.get(position).get("name"));
                    UserUtils.setAiraddr(data.get(position).get("phy_addr_did"));
                    UserUtils.setAirroute(data.get(position).get("route"));
                    if (data.get(position).get("cmd") != null) {
                        UserUtils.setAircmd(data.get(position).get("cmd"));
                    } else {
                        Log.i("xiaowen", "空调开关失败");
                        UserUtils.setAircmd("false");
                    }
                    Intent intent = new Intent(mContext, AirControl.class);
                    mContext.startActivity(intent);
                } else {
                    new SweetAlertDialog(mContext)
                            .setTitleText("请先打开电源开关")
                            .show();
                }
            }
        });

        mHolder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("xiaowen", "长按时间");

                final String[] listItems = {"修改", "删除"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


                final String username = UserUtils.getUsername();//从上页获取用户名
                final String nameLocal = data.get(position).get("name");
                final String phy_addr_did = data.get(position).get("phy_addr_did");
                final String route = data.get(position).get("route");

                builder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listItems[which].equals("修改")) {
                            Log.i("xiaowen", "修改状态");
                            Intent intent = new Intent(mContext, ModifyAirActivity.class);
                            intent.putExtra("name", nameLocal);
                            intent.putExtra("phy_addr_did", phy_addr_did);
                            intent.putExtra("route", route);
                            mContext.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteAir(position, username, "air", nameLocal, phy_addr_did, route);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        //电源
        mHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String username = UserUtils.getUsername();//从上页获取用户名
                String nameLocal = data.get(position).get("name");
                String phy_addr_did = data.get(position).get("phy_addr_did");
                String route = data.get(position).get("route");
                if (isChecked) {
                    power = "open";
                    changePowerState(username, nameLocal, phy_addr_did, route, power);

                } else {
                    power = "close";
                    changePowerState(username, nameLocal, phy_addr_did, route, power);
                }
            }
        });

        return convertView;
    }


    private void deleteAir(final int position, final String username, final String action, final String airname,
                           final String addr, final String route) {

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("name", airname);
        params.put("phy_addr_did", addr);
        params.put("route", route);
        params.put("action", action);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "airRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Log.i("xiaowen", "空调删除成功");
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "请求失败,请重新尝试", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changePowerState(final String username, final String airname, final String addr,
                                  final String route, final String airpower) {
        final HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", UserUtils.getUsername());
        params.put("name", airname);
        params.put("phy_addr_did", addr);
        params.put("route", route);
        params.put("power", airpower);

        String JsonString = JsonUtil.toJson(params);
        OkGo.post(HttpContant.getUnencryptionPath() + "airControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        ResultEntity resultEntity = JsonUtil.fromJson(s, ResultEntity.class);
                        String result = resultEntity.getResult();

                        if (result.equals("success")) {
                            Log.i("lbk", "空调电源操作成功");
                        } else {
                            Toast.makeText(mContext, "空调操作失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
