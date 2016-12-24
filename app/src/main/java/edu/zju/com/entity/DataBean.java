package edu.zju.com.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin on 2016/12/9.
 * 对应JSon数组的实体类
 */

public class DataBean {

    private String action;
    private String type;
    private String count;
    private List<DataInfo> data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DataInfo> getData() {
        return data;
    }

    public void setData(List<DataInfo> data) {
        this.data = data;
    }
}
