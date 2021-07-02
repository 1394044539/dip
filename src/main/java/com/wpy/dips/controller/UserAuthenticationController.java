package com.wpy.dips.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpy.dips.bean.Role;
import com.wpy.dips.bean.User;
import com.wpy.dips.bean.UserAuthentication;
import com.wpy.dips.dao.RoleDao;
import com.wpy.dips.service.UserAuthenticationService;
import com.wpy.dips.util.ProcResult;
import com.wpy.dips.util.UploadPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/userAu")
@Controller
public class UserAuthenticationController {

    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    RoleDao roleDao;

    /**
     * 用户个人认证
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/personalAu")
    @ResponseBody
    public ProcResult personalAu(HttpServletRequest request, HttpServletResponse response){
        //取参数
        String userid=request.getParameter("userid");
        String realname=request.getParameter("realname");
        String IDnumber=request.getParameter("IDnumber");
        //取出二进制图片
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("photo");
        //存图片
        String savaPath="static/imgs/AuImgs";
        UploadPhoto uploadPhoto=new UploadPhoto();
        String photoPath = uploadPhoto.savaPhotoPath(multipartFile, savaPath);

        //取出当前登录角色的信息
        User user = (User)request.getSession().getAttribute("userinfo");
        Role role=roleDao.findRoleInfoByRoleId(2);
        UserAuthentication userAuthentication=new UserAuthentication(user,role,IDnumber,realname,null,null,photoPath,'W');
        System.out.println(userAuthentication);
        //将当前角色信息以及提交的验证信息保存到数据库
        Integer index = userAuthenticationService.addAu(userAuthentication);
        if(index>0){
            return ProcResult.success();
        }
        return ProcResult.fail();
    }


    /**
     * 查询所有权限申请
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findAllAu")
    @ResponseBody
    public ProcResult findAllAu(@RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,@RequestParam("isPass") String isPass){
        PageHelper.startPage(pageNum,pageSize);
        List<UserAuthentication> allAu = userAuthenticationService.findAllAu(isPass);
        PageInfo<UserAuthentication> pageInfo=new PageInfo<UserAuthentication>(allAu);
        if(allAu.size()!=0){
            return ProcResult.success().bind("pageInfo",pageInfo);
        }
        return ProcResult.fail();
    }

    /**
     * 确认身份验证
     * @param userid
     * @return
     */
    @RequestMapping("/confirmAuPass")
    @ResponseBody
    public ProcResult confirmAuPass(@RequestParam("userid")int userid,@RequestParam("roleid")int roleid,@RequestParam("isPass")String isPass){
        //1.更改au表里面的isPass状态
        boolean result = userAuthenticationService.updataIsPass(userid,roleid,isPass);
        //2.把user表相应的权限给赋值
        boolean uRole = roleDao.updateRole(userid,roleid);

        if(result && uRole){
            return ProcResult.success();
        }

        return ProcResult.fail();
    }
}
