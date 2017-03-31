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
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.activity.ModifyDoorActivity;
import edu.zju.com.activity.ModifyLibrary;
import edu.zju.com.activity.RoomNActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lixiaowen on 2017/3/13.
 */

public class RoomAdapter extends BaseAdapter {
    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    private RoomAdapter roomAdapter;

    RoomAdapter.ViewHolder mHolder;

    public RoomAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }


    public final class ViewHolder {
        public TextView name;

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
        if (convertView == null) {
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.list_roomlayout, null);
            mHolder = new ViewHolder();
            mHolder.name = (TextView) convertView.findViewById(R.id.tv_roomname);
            convertView.setTag(mHolder);
        } else {
            mHolder = (RoomAdapter.ViewHolder) convertView.getTag();
        }


//        绑定数据
        mHolder.name.setText(data.get(position).get("library"));
        //门的状态



        mHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomNActivity.class);

                UserUtils.setLibraryname(data.get(position).get("library"));
                UserUtils.setLibraryid(data.get(position).get("id"));
                context.startActivity(intent);
            }
        });

        mHolder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("xiaowen", "长按时间");

                final String[] listItems = {"修改", "删除"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);


                final String username = UserUtils.getUsername();//从上页获取用户名
                final String libraryname = data.get(position).get("library");
                final String idstr = data.get(position).get("id");

                builder.setItems(listItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listItems[which].equals("修改")) {
                            Log.i("xiaowen", "修改状态");

                            Intent intent = new Intent(context, ModifyLibrary.class);
                            intent.putExtra("library", libraryname);
                            intent.putExtra("library_id", idstr);

                            context.startActivity(intent);
                        } else {
                            Log.i("xiaowen", "删除数据");
                            deleteRoom(position, username, "library",libraryname,idstr);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        return convertView;
    }

    private void deleteRoom(final int position, String username, String action, String libraryname, String idstr) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("action", action);
        params.put("library",libraryname);
        params.put("library_id",idstr);
        String JsonString = JsonUtil.toJson(params);

        OkGo.post(HttpContant.getUnencryptionPath() + "libraryRemove")//
                .tag(this)//
                .upJson(JsonString)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Map<String, String> resultEntity = JsonUtil.fromJson(s, Map.class);
                        String result = resultEntity.get("result");

                        if (result.equals("success")) {
                            Toast.makeText(context, "图书馆删除成功", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, resultEntity.get("result") + "", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(context, "服务器无响应,请稍后尝试", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
