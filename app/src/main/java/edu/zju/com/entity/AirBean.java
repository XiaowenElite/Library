package edu.zju.com.entity;

import java.util.List;

/**
 * Created by bin on 2016/12/10.
 */

public class AirBean {
    private String action;
    private String type;
    private String count;
    private List<AirInfo> data;

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

    public List<AirInfo> getData() {
        return data;
    }

    public void setData(List<AirInfo> data) {
        this.data = data;
    }
}
