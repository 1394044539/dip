package com.wpy.dips.bean;

import lombok.Data;

@Data
public class UserAuthentication {
    private int uaid;
    private User user;
    private Role role;
    private String realname;
    private String IDnumber;
    private String institutionname;
    private String institutionphone;
    private String photo;
    private char isPass;

    public UserAuthentication() {
    }

    public UserAuthentication(int uaid, User user, Role role, String realname, String IDnumber, String institutionname, String institutionphone, String photo, char isPass) {
        this.uaid = uaid;
        this.user = user;
        this.role = role;
        this.realname = realname;
        this.IDnumber = IDnumber;
        this.institutionname = institutionname;
        this.institutionphone = institutionphone;
        this.photo = photo;
        this.isPass = isPass;
    }

    public UserAuthentication(User user, Role role, String realname, String IDnumber, String institutionname, String institutionphone, String photo, char isPass) {
        this.user = user;
        this.role = role;
        this.realname = realname;
        this.IDnumber = IDnumber;
        this.institutionname = institutionname;
        this.institutionphone = institutionphone;
        this.photo = photo;
        this.isPass = isPass;
    }
}
