package edu.zju.com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import edu.zju.com.activity.ModifyTempActivity;
import edu.zju.com.entity.TemHumBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lixiaowen on 16/12/28.
 */

public class TemHumAdapter extends BaseAdapter {

    private List<TemHumBean.DataBean> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private ViewHolder mHolder;

    public TemHumAdapter(Context context, List<TemHumBean.DataBean> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
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
            convertView = layoutInflater.inflate(R.layout.temhumiditylist, null);
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_temname);
            mHolder.tempture = (TextView) convertView.findViewById(R.id.tv_temp);
            mHolder.humidity = (TextView) convertView.findViewById(R.id.tv_humidity);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.name.setText(data.get(position).getName().toString());

        String result = data.get(position).getResult().toString();
        if (result.equals("success")) {
            mHolder.tempture.setText(data.get(position).getTemp().toString());
            mHolder.humidity.setText(data.get(position).getHum().toString());
        } else {
            mHolder.tempture.setText("***");
            mHolder.humidity.setText("***");
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

                            Intent intent = new Intent(context, ModifyTempActivity.class);
                            intent.putExtra("name", nameLocal);
                            intent.putExtra("phy_addr_did", phy_addr_did);
                            intent.putExtra("route", route);
                            context.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteTemp(position, "temphun", nameLocal, phy_addr_did, route);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        return convertView;
    }

    private void deleteTemp(final int position, String action, String nameLocal,
                            String phy_addr_did, String route) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", UserUtils.getUsername());
        params.put("name", nameLocal);
        params.put("phy_addr_did", phy_addr_did);
        params.put("route", route);
        params.put("type", action);
        params.put("library_id", UserUtils.getLibraryid());

        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "temphumRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Log.i("xiaowen", "温湿度设备删除成功");
                            Toast.makeText(context, "温湿度设备删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show();
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


    public final class ViewHolder {
        public TextView name;
        public TextView tempture;
        public TextView humidity;
    }


}
