package edu.zju.com.entity;

/**
 * Created by bin on 2016/12/10.
 */

public class LightInfo {
    private String name;
    private String phy_addr_did;
    private String route;
    private String cmd;

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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
