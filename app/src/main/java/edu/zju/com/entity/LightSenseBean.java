package edu.zju.com.entity;

import java.util.List;

/**
 * Created by lixiaowen on 16/12/28.
 */

public class LightSenseBean {

    /**
     * action : synchro
     * type : lightsense
     * count : 2
     * data : [{"name":"","phy_addr_did":"111","route":" ","value":" ","result":" "},{"name":"","phy_addr_did":"333","route":" ","value":" ","result":""}]
     */

    private String action;
    private String type;
    private String count;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name :
         * phy_addr_did : 111
         * route :
         * value :
         * result :
         */

        private String name;
        private String phy_addr_did;
        private String route;
        private String value;
        private String result;

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
