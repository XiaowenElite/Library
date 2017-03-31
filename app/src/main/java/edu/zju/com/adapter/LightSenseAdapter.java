package edu.zju.com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.activity.ModifyDoorActivity;
import edu.zju.com.activity.ModifyLightSenseActivity;
import edu.zju.com.entity.LightSenseBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lixiaowen on 16/12/28.
 */

public class LightSenseAdapter extends BaseAdapter {

    private List<LightSenseBean.DataBean> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private ViewHolder mHolder;

    public LightSenseAdapter(Context context,List<LightSenseBean.DataBean> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listlightsense,null);
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_lsname);
            mHolder.value = (TextView) convertView.findViewById(R.id.tv_lsvalue);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder)convertView.getTag();
        }

        mHolder.name.setText(data.get(position).getName().toString());

        String result = data.get(position).getResult().toString();
        if (result.equals("success")){
            mHolder.value.setText(data.get(position).getValue().toString());
        }else {
            mHolder.value.setText("***");
        }


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] listItems = {"修改", "删除"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final String nameLocal = data.get(position).getName();
                final String phy_addr_did = data.get(position).getPhy_addr_did();
                final String route = data.get(position).getRoute();

                builder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listItems[which].equals("修改")) {
                            Log.i("xiaowen", "修改状态");

                            Intent intent = new Intent(context, ModifyLightSenseActivity.class);
                            intent.putExtra("name", nameLocal);
                            intent.putExtra("phy_addr_did", phy_addr_did);
                            intent.putExtra("route", route);
                            context.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteLightSense(position, "lightsense", nameLocal, phy_addr_did, route);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView value;
    }

    private void deleteLightSense(final int position, String action, String nameLocal,
                            String phy_addr_did, String route) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", UserUtils.getUsername());
        params.put("name", nameLocal);
        params.put("phy_addr_did", phy_addr_did);
        params.put("route", route);
        params.put("type", action);
        params.put("library_id", UserUtils.getLibraryid());

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "lightsenseRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Log.i("xiaowen", "光感设备删除成功");
                            Toast.makeText(context, "光感设备删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "请求失败,请重新尝试："+result.toString(), Toast.LENGTH_SHORT).show();
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


}
