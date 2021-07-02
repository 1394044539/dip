package com.wpy.dips.realm;

import com.wpy.dips.bean.User;
import com.wpy.dips.service.PermissionService;
import com.wpy.dips.service.RoleService;
import com.wpy.dips.service.UserService;
import com.wpy.dips.vo.LoginUserVo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class PhoneRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //授权器:
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //获得登陆主体
        LoginUserVo loginUserVo=(LoginUserVo) principalCollection.getPrimaryPrincipal();
        //获取角色列表
        Set<String> roles=loginUserVo.getRoles();
        //获取权限列表
        Set<String> permissions=loginUserVo.getPermissions();
        if(roles!=null && roles.size()>0){
            info.setRoles(roles);
        }
        if(permissions!=null && permissions.size()>0){
            info.setStringPermissions(permissions);
        }
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        PhoneToken token=null;
        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if(authenticationToken instanceof PhoneToken){
            token = (PhoneToken) authenticationToken;
        }else{
            return null;
        }
        String phone = (String) token.getPrincipal();

        User user = userService.findUserByPhone(phone);
        if(user==null){
            throw new UnknownAccountException();
        }
        Set<String> roles = roleService.findRoleListByUserId(user.getUserid());
        Set<String> permission = permissionService.findPermissionListByUserId(user.getUserid());
        LoginUserVo loginUserVo=new LoginUserVo(user,roles,permission);
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(loginUserVo,phone,getName());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof PhoneToken;
    }
}
