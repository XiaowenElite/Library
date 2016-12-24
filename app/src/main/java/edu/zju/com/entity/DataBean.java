package edu.zju.com.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by bin on 2016/12/9.
 * 对应JSon数组的实体类
 */

public class DataBean {

    private String action;
    private String type;
    private String count;
    private List<Map<String,String>> data;

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

    public List<Map<String,String>> getData() {
        return data;
    }

    public void setData(List<Map<String,String>> data) {
        this.data = data;
    }
}
