package com.terabits.meta.po.Huawei;

public class HuaweiToken {
    private int id;
    private String huaweiToken;
    private String gmtCreate;
    private String gmtModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHuaweiToken() {
        return huaweiToken;
    }

    public void setHuaweiToken(String huaweiToken) {
        this.huaweiToken = huaweiToken;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "HuaweiToken{" +
                "id=" + id +
                ", huaweiToken='" + huaweiToken + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                '}';
    }
}
