package edu.zju.com.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import java.util.Objects;

import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

import static android.widget.Toast.makeText;
import static edu.zju.com.activity.MyAdpter.Zujian.*;

/**
 * Created by bin on 2016/11/20.
 */

public class MyAdpter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public MyAdpter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    /*
    * 组件集合，对应listlayout.xml中的控件
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
            convertView= layoutInflater.inflate(R.layout.listlayout,null);
            zujian.name= (TextView) convertView.findViewById(R.id.tv_doorname);
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
        //门的状态
        String status = (String) data.get(position).get("cmd");
        Log.i("lbk",status);
        if(status.equals("open")){
            zujian.onOff.setChecked(true);
            Log.i("lbk","门的状态为打开");
        }
        else if(status.equals("close")){
            zujian.onOff.setChecked(false);
            Log.i("lbk","门的状态为关闭");
        }

        zujian.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                {
                    //获取对应的标识，作为当前点击门的标识传递
                    String username = (String) data.get(position).get("username");//从上页获取用户名
                    String nameLocal = (String) data.get(position).get("name");
                    String phy_addr_did =(String) data.get(position).get("phy_addr_did");
                    String route = (String) data.get(position).get("route");
                    String cmd = "open";

                    testpost(username,nameLocal,phy_addr_did,route,cmd);
                    //从接收到的值获取，再把对应的门传回服务器
                    Log.i("liaobinkai","kaimen");
                }
                else{
                    //没选中状态
                    //获取对应的标识，作为当前点击门的标识传递
                    String username = (String) data.get(position).get("username");//从上页获取用户名
                    String nameLocal = (String) data.get(position).get("name");
                    String phy_addr_did =(String) data.get(position).get("phy_addr_did");
                    String route = (String) data.get(position).get("route");
                    String cmd = "close";

                    testpost(username,nameLocal,phy_addr_did,route,cmd);
                    //从接收到的值获取，再把对应的门传回服务器
                    Log.i("liaobinkai","guanmen");
                }
            }
        });


        return convertView;
    }
    private void testpost(String params1,String params2,String params3,String params4,String params5){
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("username", params1);
        params.put("name", params2);
        params.put("phy_addr_did", params3);
        params.put("route", params4);
        params.put("cmd",params5);

        String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
        OkGo.post(HttpContant.getUnencryptionPath()+"doorControl")//
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
                            Log.i("lbk","门操作成功");
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
                        Log.i("xiaowen","error");
                    }
                });
    }

}
