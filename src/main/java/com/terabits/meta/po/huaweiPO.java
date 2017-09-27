package com.terabits.meta.po;

public class huaweiPO {
    int id;
    String huawei_token;
    String gmtcreate;
    String gmtmodified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHuawei_token() {
        return huawei_token;
    }

    public void setHuawei_token(String huawei_token) {
        this.huawei_token = huawei_token;
    }

    public String getGmtcreate() {
        return gmtcreate;
    }

    public void setGmtcreate(String gmtcreate) {
        this.gmtcreate = gmtcreate;
    }

    public String getGmtmodified() {
        return gmtmodified;
    }

    public void setGmtmodified(String gmtmodified) {
        this.gmtmodified = gmtmodified;
    }

    @Override
    public String toString() {
        return "huaweiPO{" +
                "id=" + id +
                ", huawei_token='" + huawei_token + '\'' +
                ", gmtcreate='" + gmtcreate + '\'' +
                ", gmtmodified='" + gmtmodified + '\'' +
                '}';
    }
}
