package com.wpy.dips.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpy.dips.bean.Role;
import com.wpy.dips.bean.User;

import com.wpy.dips.bean.UserAuthentication;
import com.wpy.dips.realm.PhoneToken;
import com.wpy.dips.service.RoleService;
import com.wpy.dips.service.UserAuthenticationService;
import com.wpy.dips.service.UserService;
import com.wpy.dips.util.ProcResult;
import com.wpy.dips.vo.LoginUserVo;
import com.zhenzi.sms.ZhenziSmsClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserAuthenticationService userAuthenticationService;


    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public ProcResult login(User user, HttpServletRequest request){
        HttpSession session=request.getSession();
        //获得用户
        Subject subject= SecurityUtils.getSubject();
        //创建口令
        try {
            UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());
            subject.login(token);
            LoginUserVo loginUserVo=(LoginUserVo)subject.getPrincipal();
            System.out.println(loginUserVo.toString());

            //存入session
            User userSession=loginUserVo.getUser();
            Role roleSession = roleService.findRoleByUserId(userSession.getUserid());
            userSession.setRole(roleSession);
            session.setAttribute("userinfo",userSession);
            session.setAttribute("permissionsinfo",loginUserVo.getPermissions());
            //判断权限
            return ProcResult.success().bind("role",roleSession.getRoleid());

        } catch (AuthenticationException e) {
            return ProcResult.fail().bind("errorInfo","账号或密码错误！");
            //e.printStackTrace();
        }
    }

    /**
     * 手机验证码登录
     */
    @PostMapping("/loginByPhone")
    @ResponseBody
    public ProcResult loginByPhone(@RequestParam("phone") String phone,@RequestParam("Vcode")String vcode, HttpServletRequest request){
        JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
        if(json==null){
            return ProcResult.fail().bind("errorInfo","验证码错误");
        }
        if(!json.getString("verifyCode").equals(vcode)){
            return ProcResult.fail().bind("errorInfo","验证码错误");
        }
        if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            return ProcResult.fail().bind("errorInfo","验证码过期");
        }

            HttpSession session=request.getSession();
            //获得用户
            Subject subject= SecurityUtils.getSubject();
            //创建口令
            try {

                PhoneToken token=new PhoneToken(phone);
                subject.login(token);
                LoginUserVo loginUserVo=(LoginUserVo)subject.getPrincipal();
                //存入session
                User userSession=loginUserVo.getUser();
                Role roleSession = roleService.findRoleByUserId(userSession.getUserid());
                userSession.setRole(roleSession);
                session.setAttribute("userinfo",userSession);
                session.setAttribute("permissionsinfo",loginUserVo.getPermissions());
                //判断权限
                return ProcResult.success().bind("role",roleSession.getRoleid());

            } catch (AuthenticationException e) {
                return ProcResult.fail().bind("errorInfo","该手机号未注册");
            }

    }

    /**
     * 注册账号
     */
    @PostMapping("/reg")
    @ResponseBody
    public ProcResult reg(@Valid User user,BindingResult bindingResult,@RequestParam("verifyCode")String verifyCode,HttpServletRequest request){
        Map<String,String> msgMap=new HashMap<String, String>();
        //正则校验属性
        if(bindingResult.hasErrors()){
            //拿到错误原因
            for(FieldError fieldError:bindingResult.getFieldErrors()){
                //错误的参数名
                String field=fieldError.getField();
                //错误的原因（消息）
                String errMsg=fieldError.getDefaultMessage();
                msgMap.put(field,errMsg);
            }
            return ProcResult.fail().bind("errorInfo",msgMap);
            //前端取值，result.data.errorInfo.field
        }

        if(userService.findUserByName(user.getUsername())!=null){//验证唯一性
            msgMap.put("username","该账号已被注册");
            return ProcResult.fail().bind("errorInfo",msgMap);
        }else if(userService.findUserByPhone(user.getPhone())!=null){
            msgMap.put("phone","该手机已被注册");
            return ProcResult.fail().bind("errorInfo",msgMap);
        }else {
            //验证码
            JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
            if(json==null){
                msgMap.put("Vcode","验证码错误");
                return ProcResult.fail().bind("errorInfo",msgMap);
            }
            if(!json.getString("verifyCode").equals(verifyCode)){
                msgMap.put("Vcode","验证码错误");
                return ProcResult.fail().bind("errorInfo",msgMap);
            }
            if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
                msgMap.put("Vcode","验证码过期");
                return ProcResult.fail().bind("errorInfo",msgMap);
            }
            //密码加密
            Md5Hash md5Hash=new Md5Hash(user.getPassword(),user.getUsername(),2);
            user.setPassword(md5Hash.toString());

            //正式注册并且授权
            boolean b = userService.addUser(user);
            boolean b1 = roleService.setRoleByUserId(user.getUserid(), 1);
            if(b && b1){
                System.out.println("成功");
                return ProcResult.success();
            }else{
                System.out.println("失败");
                return  ProcResult.fail();
            }
        }

    }


    /**
     * 找回密码
     */
    @PostMapping("/forgetPassword")
    @ResponseBody
    public ProcResult forgetPassword(@RequestParam("phone")String phone,@RequestParam("Vcode")String vcode,HttpServletRequest request){
        //判断验证码对不对
        JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
        if(json==null){
            return ProcResult.fail().bind("errorInfo","验证码错误");
        }
        if(!json.getString("verifyCode").equals(vcode)){
            return ProcResult.fail().bind("errorInfo","验证码错误");
        }
        if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            return ProcResult.fail().bind("errorInfo","验证码过期");
        }
        //根据手机号查询用户
        User userByPhone = userService.findUserByPhone(phone);
        if(userByPhone!=null){
            return ProcResult.success();
        }
        return ProcResult.fail().bind("errorInfo","该手机号未注册");
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public ProcResult updatePassword(@Valid User user,BindingResult bindingResult,HttpServletRequest request){
        Map<String,String> msgMap=new HashMap<String, String>();
        if(bindingResult.hasErrors()){
            //拿到错误原因
            for(FieldError fieldError:bindingResult.getFieldErrors()){
                //错误的参数名
                String field=fieldError.getField();
                //错误的原因（消息）
                String errMsg=fieldError.getDefaultMessage();
                msgMap.put(field,errMsg);
            }
            return ProcResult.fail().bind("errorInfo",msgMap);
            //前端取值，result.data.errorInfo.field
        }
        String newPassword=user.getPassword();
        String phone=user.getPhone();
        User userByPhone = userService.findUserByPhone(phone);
        if(userByPhone==null){
            msgMap.put("phone","该手机号未注册");
            return ProcResult.fail().bind("errorInfo",msgMap);
        }
        //密码加密
        Md5Hash md5Hash=new Md5Hash(newPassword,userByPhone.getUsername(),2);
        userByPhone.setPassword(md5Hash.toString());
        boolean b = userService.updatePassword(userByPhone);
        if(b){
            return ProcResult.success();
        }
        return ProcResult.fail();
    }

    /**
     * 通过手机号获取验证码
     */
    @PostMapping("/getVC")
    @ResponseBody
    public ProcResult getVC(@Valid User user,BindingResult bindingResult, HttpServletRequest request){
        //正则校验
        if(bindingResult.hasErrors()){
            //拿到错误原因
            String errorField=bindingResult.getFieldError().getField();
            String errorInfo=bindingResult.getFieldError().getDefaultMessage();
            return ProcResult.fail().bind("errorField",errorField).bind("errorInfo",errorInfo);
        }
        try {
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //发送短信
            ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","104208","9c2dd645-a754-4d7d-a6fd-a7e3fbe2572a");
            Map<String, String> params = new HashMap<String, String>();
            params.put("message", "验证码为: "+verifyCode);
            params.put("number", user.getPhone());
            String result = client.send(params);
            json = JSONObject.parseObject(result);
            if(json.getIntValue("code") != 0)//发送短信失败
                return ProcResult.fail().bind("errorInfo","发送失败，请稍后再试");
            //将验证码存到session中,同时存入创建时间
            //以json存放，这里使用的是阿里的fastjson
            HttpSession session = request.getSession();
            json = new JSONObject();
            json.put("verifyCode", verifyCode);
            json.put("createTime", System.currentTimeMillis());
            // 将认证码存入SESSION
            request.getSession().setAttribute("verifyCode", json);
            return ProcResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ProcResult.fail().bind("errorInfo","发送失败，请稍后再试");
    }


    /**
     * 展示用户列表
     * @param pageNum
     * @return
     */
    @GetMapping("/findAllUser/{pageNum}")
    @ResponseBody
    public ProcResult findAllUser(@PathVariable(value = "pageNum",required = false)int pageNum){
        PageHelper.startPage(pageNum,10);
        List<User> allUser = userService.findAllUser();
        PageInfo<User> pageInfo=new PageInfo<User>(allUser);
        if(allUser.size()!=0){
            return ProcResult.success().bind("pageInfo",pageInfo);
        }
        return ProcResult.fail();
    }

    /**
     * 页面跳转
     */
    @RequestMapping("/jumpIndex")
    public String jumpIndex(){return "index";}
    @RequestMapping("/jumpLogin")
    public String jumpLogin(){return "login";}
    @RequestMapping("/jumpReg")
    public String jumpReg(){return "reg";}
    @RequestMapping("/jumpError")
    public String jumpError(){return "error";}

    @RequestMapping("/jumpMain")
    public String jumpMain(){
        return "userPage/main";
    }

    @GetMapping("/jumpUserInfo/{tabs}")
    public String jumpUserInfo(@PathVariable("tabs")String tabs, Model model,HttpServletRequest request){
        model.addAttribute("tabs",tabs);
        if(tabs.equals("4") || tabs.equals("5") || tabs.equals("6")){
            User user = (User)request.getSession().getAttribute("userinfo");
            UserAuthentication userAuInfo = userAuthenticationService.findUserAuInfo(user.getUserid());
            if(userAuInfo!=null){
                System.out.println(userAuInfo);
                model.addAttribute("userAuInfo",userAuInfo);
            }
        }

        return "userPage/userInfo";
    }

    @GetMapping("/jumpBackground/{tabs}")
    public String jumpAdmin(@PathVariable("tabs")String tabs, Model model){
        model.addAttribute("tabs",tabs);
        return "adminPage/background";
    }
}
