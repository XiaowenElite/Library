package edu.zju.com.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.zju.com.entity.LightSenseBean;
import edu.zju.com.librarycontroller.R;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView value;
    }


}
