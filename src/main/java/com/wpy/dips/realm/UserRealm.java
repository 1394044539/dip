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

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 表示授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("授权校验");
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
        System.out.println(info);
        return info;
    }

    /**
     * 表示身份验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("身份验证");
        //1.得到当前用户
        String username=authenticationToken.getPrincipal().toString();
        String password=authenticationToken.getCredentials().toString();
        User user=userService.findUserByName(username);
        if(user==null){
            user=userService.findUserByPhone(username);
        }
        if(user!=null){
            Set<String> roles = roleService.findRoleListByUserId(user.getUserid());
            Set<String> permission = permissionService.findPermissionListByUserId(user.getUserid());
            LoginUserVo loginUserVo=new LoginUserVo(user,roles,permission);
            //使用用户名作为盐值
            ByteSource salt=ByteSource.Util.bytes(user.getUsername());
            //创建身份验证对象(验证类)
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(loginUserVo,user.getPassword(),salt,getName());
            return info;
        }
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
