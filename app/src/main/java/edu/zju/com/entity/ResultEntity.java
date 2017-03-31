package edu.zju.com.entity;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by lixiaowen on 16/12/15.
 */

public class ResultEntity {
    private String action;
    private String name;
    private String phy_addr_did;
    private String route;
    private String result;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    private Map<String,String> data;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhy_addr_did() {
        return phy_addr_did;
    }

    public void setPhy_addr_did(String phy_addr_did) {
        this.phy_addr_did = phy_addr_did;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
