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

import edu.zju.com.activity.ModifyLightActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/11/20.
 */

public class LightMyAdpter extends BaseAdapter {
    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private ViewHolder mHolder;

    public LightMyAdpter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /*
    * 组件集合，对应list.xml中的控件
    * @author bin
    * */
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
            convertView = layoutInflater.inflate(R.layout.listlayoutlight, null);//对应list布局文件
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_lightname);
            mHolder.onOff = (ToggleButton) convertView.findViewById(R.id.tg_onOff);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
//        绑定数据
        mHolder.name.setText((String) data.get(position).get("name"));
        mHolder.onOff.setBackgroundResource(R.drawable.ios7_btn);
        //灯的状态
        String status = (String) data.get(position).get("cmd");
        if (status != null) {
            if (status.equals("open")) {
                mHolder.onOff.setChecked(true);
            } else if (status.equals("close")) {
                mHolder.onOff.setChecked(false);
            }
        } else {
            mHolder.onOff.setChecked(false);
        }

        mHolder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String username = UserUtils.getUsername();//从上页获取用户名
                String nameLocal = (String) data.get(position).get("name");
                String phy_addr_did = (String) data.get(position).get("phy_addr_did");
                String route = (String) data.get(position).get("route");

                if (isChecked) {
                    //同步设备时获取到的数据
                    String cmd = "open";
                    controlLight(username, nameLocal, phy_addr_did, route, cmd);
                    Log.i("liaobinkai", "kaideng");

                } else {
                    //没选中状态
                    String cmd = "close";
                    controlLight(username, nameLocal, phy_addr_did, route, cmd);
                    Log.i("liaobinkai", "guandeng");
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
                            Intent intent = new Intent(context, ModifyLightActivity.class);
                            intent.putExtra("name", nameLocal);
                            intent.putExtra("phy_addr_did", phy_addr_did);
                            intent.putExtra("route", route);
                            context.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteLight(position, username, "light", nameLocal, phy_addr_did, route);
                        }
                    }


                });
                builder.create().show();
                return true;
            }
        });

        return convertView;
    }

    private void deleteLight(final int position, String username, String action,
                             String nameLocal, String phy_addr_did, String route) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("name", nameLocal);
        params.put("phy_addr_did", phy_addr_did);
        params.put("route", route);
        params.put("action", action);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "lightRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Log.i("xiaowen", "灯删除成功");
                            Toast.makeText(context, "灯删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
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

    private void controlLight(String params1, String params2, String params3, String params4, String params5) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("name", params2);
        params.put("phy_addr_did", params3);
        params.put("route", params4);
        params.put("cmd", params5);

        String JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath() + "lightControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.i("xiaowen", "result" + s);
                        //转换成Map
                        Map data = JsonUtil.fromJson(s, Map.class);
                        //get方法直接获取key对应的value
                        String result = (String) data.get("result");
                        if (result.equals("success")) {
                            Log.i("lbk", "灯操作成功");
                            Toast.makeText(context, "灯操作成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "灯操作失败", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("lbk","result="+result);


                    }
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("xiaowen", "error");
                        Toast.makeText(context, "服务器无响应,请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
