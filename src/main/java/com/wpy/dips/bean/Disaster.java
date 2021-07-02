package com.wpy.dips.bean;

import lombok.Data;

@Data
public class Disaster {
    private int disasterid;
    private String name;
    private String type;
    private String sysnopsis;
    private String time;
    private String address;
    private String photo;
    private int hot;
    private int userid;
    private User user;

    public Disaster() {
    }

    public Disaster(String name, String sysnopsis,String time,String address,String type, String photo) {
        this.name = name;
        this.type=type;
        this.sysnopsis = sysnopsis;
        this.time=time;
        this.address=address;
        this.photo = photo;
    }

    public Disaster(int disasterid, String name, String sysnopsis,String time,String address,String type, String photo) {
        this.disasterid = disasterid;
        this.name = name;
        this.type=type;
        this.sysnopsis = sysnopsis;
        this.time=time;
        this.address=address;
        this.photo = photo;
    }

    public Disaster(String name, String sysnopsis, String time, String address, String type,String photo,User user) {
        this.name = name;
        this.type=type;
        this.sysnopsis = sysnopsis;
        this.time=time;
        this.address=address;
        this.photo=photo;
        this.user=user;
    }

    public Disaster(int disasterid, String name, String type, String sysnopsis, String time, String address, String photo, int hot, int userid) {
        this.disasterid = disasterid;
        this.name = name;
        this.type = type;
        this.sysnopsis = sysnopsis;
        this.time = time;
        this.address = address;
        this.photo = photo;
        this.hot = hot;
        this.userid = userid;
    }

    public Disaster(int disasterid, String name, String type, String sysnopsis, String time, String address, String photo, int hot, int userid, User user) {
        this.disasterid = disasterid;
        this.name = name;
        this.type = type;
        this.sysnopsis = sysnopsis;
        this.time = time;
        this.address = address;
        this.photo = photo;
        this.hot = hot;
        this.userid = userid;
        this.user = user;
    }
}
