package com.wpy.dips.vo;

import com.wpy.dips.bean.User;
import lombok.Data;

import java.util.Set;

@Data
public class LoginUserVo {
    User user;
    //采用set去除重复
    Set<String> roles;
    Set<String> permissions;

    public LoginUserVo() {
    }

    public LoginUserVo(User user, Set<String> roles, Set<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }
}
