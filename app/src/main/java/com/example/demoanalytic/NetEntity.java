package com.example.demoanalytic;



        import java.io.Serializable;

public class NetEntity implements Serializable {

    /**
     * status : 1
     * msg : connect succeed!
     * PRODUCTID : zcz001
     * DEVICEID : zcz001100002
     * name : wanyeah
     */

    private int status;
    private String msg;
    private String PRODUCTID;
    private String DEVICEID;
    private String name;
    private String ssid;
    private String password;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPRODUCTID() {
        return PRODUCTID;
    }

    public void setPRODUCTID(String PRODUCTID) {
        this.PRODUCTID = PRODUCTID;
    }

    public String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "NetEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", PRODUCTID='" + PRODUCTID + '\'' +
                ", DEVICEID='" + DEVICEID + '\'' +
                ", name='" + name + '\'' +
                ", ssid='" + ssid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
