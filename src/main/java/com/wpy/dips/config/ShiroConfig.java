package com.wpy.dips.config;

import com.wpy.dips.realm.PhoneRealm;
import com.wpy.dips.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对照application-shiro.xml配置的java.class
 * 相当于把class文件当做.xml文件
 */

@Configuration
public class ShiroConfig {

    //申请凭证匹配器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    //声明自定义Realm
    @Bean
    public UserRealm myUserPealm(){
        UserRealm userRealm=new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }
    @Bean
    public PhoneRealm phoneRealm(){
        PhoneRealm phoneRealm = new PhoneRealm();
        return phoneRealm;
    }

    //配置安全管理器
    @Bean
    public SecurityManager securityManager(UserRealm myUserPealm,PhoneRealm phoneRealm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        List<Realm> realms=new ArrayList<Realm>();
        realms.add(myUserPealm);
        realms.add(phoneRealm);
        //securityManager.setRealm(myUserPealm());
        securityManager.setRealms(realms);
        return securityManager;
    }



    //配置过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/user/jumpMain");
        shiroFilterFactoryBean.setSuccessUrl("/user/jumpMain");
        shiroFilterFactoryBean.setUnauthorizedUrl("/templates/error.html");

        Map<String,String> map=new HashMap<String,String>();
        map.put("/","anon");
        //放过静态资源
        map.put("/static/**/*","anon");
        //user的所有请求放过
        map.put("/user/**","anon");
        //disaster的请求放过
        map.put("/disaster/**/*","anon");
        //登出
        map.put("/logout","logout");
        //其余都需要验证
        map.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
