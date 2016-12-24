package edu.zju.com.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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

import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by bin on 2016/11/20.
 */

public class AirMyAdpter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public AirMyAdpter(Context context, List<Map<String, Object>> data){
        this.mContext=context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    /*
    * 组件集合，对应list.xml中的控件
    * @author bin
    * */
    public  final  class Zujian{
        public TextView nameBack;
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
        Zujian zujian = null;
        if(convertView==null){
            zujian = new Zujian();
            //获得组件，实例化组件
            convertView= layoutInflater.inflate(R.layout.listlayoutair,null);//对应list布局文件
            zujian.name= (TextView) convertView.findViewById(R.id.tv_airname);
//            zujian.nameBack = (TextView) convertView.findViewById(R.id.tv_nameBack);
            zujian.onOff = (ToggleButton) convertView.findViewById(R.id.tg_onOff);
            convertView.setTag(zujian);
        }else {
            zujian = (Zujian) convertView.getTag();
        }
//        绑定数据
        zujian.name.setText((String) data.get(position).get("name"));
//        zujian.nameBack.setBackgroundResource((Integer) data.get(position).get("nameBack"));
        zujian.onOff.setBackgroundResource((Integer) data.get(position).get("onOff"));
        //空调的电源状态
        String status = (String) data.get(position).get("power");
        Log.i("lbk",status);
        if(status.equals("open")){
            zujian.onOff.setChecked(true);
            Log.i("lbk","空调的状态为打开");
        }
        else if(status.equals("close")){
            zujian.onOff.setChecked(false);
            Log.i("lbk","空调的状态为关闭");
        }
        zujian.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                {
                    //从接收到的值获取，再把对应的门传回服务器

                    //获取对应的标识，作为当前点击门的标识传递
                    String username = (String) data.get(position).get("username");//从上页获取用户名
                    String nameLocal = (String) data.get(position).get("name");
                    String phy_addr_did =(String) data.get(position).get("route");
                    String route =(String) data.get(position).get("phy_addr_did");
                    String power = "open";
                    String cmd = (String) data.get(position).get("cmd");//cmd是用于传到控制界面
                    Log.i("lbk","kongtiaokai");
                    testpost(username,nameLocal,phy_addr_did,route,power,cmd);



//                    AirActivity activity = new AirActivity();
//                    Intent intent;
//                    intent = new Intent(activity.getBaseContext(), AirControl.class);
//                    context.startActivity(intent);

                }
                else{
                    String username = (String) data.get(position).get("username");//从上页获取用户名
                    String nameLocal = (String) data.get(position).get("name");
                    String phy_addr_did =(String) data.get(position).get("phy_addr_did");
                    String route =(String) data.get(position).get("phy_addr_did");
                    String power = "close";
                    String cmd = (String) data.get(position).get("cmd");

                    testpost(username,nameLocal,phy_addr_did,route,power,cmd);
                }
            }
        });

        return convertView;
    }
    private void testpost(final String params1, final String params2, final String params3, final String params4, final String params5, final String params6){
        final HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("name", params2);
        params.put("phy_addr_did", params3);
        params.put("route", params4);
        params.put("power", params5);
        params.put("cmd", params6);

        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"airControl")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        Log.i("xiaowen","result"+s);
                        //转换成Map
                        Map data = JsonUtil.fromJson(s,Map.class);
                        //get方法直接获取key对应的value
                        String result = (String) data.get("result");
                        if(result.equals("success")){
                            Log.i("lbk","空调电源操作成功");
                            //跳转到空调控制
                            //将空调开关状态传递给控制界面
                            Intent intent = new Intent(mContext,AirControl.class);
                            //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                            Bundle bundle = new Bundle();
                            bundle.putString("cmd", params6);//将空调状态传给控制界面进行状态记录
                            bundle.putString("username",params1);
                            bundle.putString("name",params2);
                            bundle.putString("phy_addr_did",params3);
                            bundle.putString("route",params4);
                            //此处使用putExtras，接受方就响应的使用getExtras
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);

                        }
                        else{
                            Toast.makeText(mContext,"空调操作失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("xiaowen","error");
                    }
                });
    }

}
