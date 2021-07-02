package com.wpy.dips.bean;

import lombok.Data;

@Data
public class Role {
    int roleid;
    String rolename;

    public Role() { }

    public Role(int roleid, String rolename) {
        this.roleid = roleid;
        this.rolename = rolename;
    }
}
