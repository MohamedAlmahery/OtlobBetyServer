package com.phoenix.otlobbetyserver.Model;

public class Push {
    private String phone;
    private String name;
    private String idnum;

    public Push() {
    }

    public Push(String phone, String name, String idnum) {
        this.phone = phone;
        this.name = name;
        this.idnum = idnum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }
}
