package edu.zju.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.zju.com.entity.LightSenseBean;
import edu.zju.com.entity.TemHumBean;
import edu.zju.com.librarycontroller.R;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView tempture;
        public TextView humidity;
    }


}
