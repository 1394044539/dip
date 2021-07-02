package com.wpy.dips.bean;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class User {
    int userid;
    @Pattern(regexp = "^[a-zA-Z0-9]\\w{5,17}$",message = "账户必须是6-18位字母和数字组合")
    String username;
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5]{6,18}$",message = "密码必须是6-18位且不含特殊字符")
    String password;
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "手机号格式不正确")
    String phone;
    String virtualname;
    Role role;
    String photo;
}
