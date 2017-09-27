package com.terabits.meta.po.Huawei;

public class NotifyDataPO {
    private int id;
    private String content;
    private String gmtCreat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreat() {
        return gmtCreat;
    }

    public void setGmtCreat(String gmtCreat) {
        this.gmtCreat = gmtCreat;
    }

    @Override
    public String toString() {
        return "NotifyDataPO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", gmtCreat='" + gmtCreat + '\'' +
                '}';
    }
}
