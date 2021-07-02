package com.wpy.dips.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wpy.dips.bean.DisData;
import com.wpy.dips.bean.Disaster;
import com.wpy.dips.bean.User;
import com.wpy.dips.service.DisDataService;
import com.wpy.dips.service.DisasterService;
import com.wpy.dips.util.ProcResult;
import com.wpy.dips.util.UploadPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/disaster")
public class DisasterController {

    @Autowired
    DisasterService disasterService;
    @Autowired
    DisDataService disDataService;

    @RequestMapping("/addDisaster")
    @ResponseBody
    public ProcResult addDisaster(HttpServletRequest request, HttpServletResponse response){
        //取出form-data中的值
        String name=request.getParameter("name");
        String sysnopsis=request.getParameter("sysnopsis");
        String address=request.getParameter("address").replace("0","");
        String time=request.getParameter("time");
        String type=request.getParameter("type");
        //取出二进制图片
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("photo");
        //存入对应的地址中
        String savaPath="static/imgs/disasterImgs/";
        UploadPhoto uploadPhoto=new UploadPhoto();
        //拿到文件地址
        String photoPath = uploadPhoto.savaPhotoPath(multipartFile, savaPath);

        User user = (User)request.getSession().getAttribute("userinfo");
        Disaster disaster=new Disaster(name,sysnopsis,time,address,type,photoPath,user);
        System.out.println(disaster);
        int iRet = disasterService.addDisaster(disaster);
        if(iRet>0){
            return ProcResult.success().bind("id",disaster.getDisasterid());
        }
        return ProcResult.fail();
    }

    @RequestMapping("/findAllDisaster")
    @ResponseBody
    public ProcResult findAllDisaster(@RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "5")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Disaster> allDisaster = disasterService.findAllDisaster();
        PageInfo<Disaster> pageInfo=new PageInfo<Disaster>(allDisaster);
        if(allDisaster.size()!=0){
            return ProcResult.success().bind("pageInfo",pageInfo);
        }

        return ProcResult.fail();
    }

    @RequestMapping("/findDisasterById")
    @ResponseBody
    public ProcResult findDisasterById(@RequestParam("id")int id){
        Disaster disaster = disasterService.findDisasterById(id);
        System.out.println(disaster);
        if(disaster!=null){
            return ProcResult.success().bind("disaster",disaster);
        }
        return ProcResult.fail();
    }

    @RequestMapping("/findDisasterBySearch")
    @ResponseBody
    public ProcResult findDisasterBySearch(@RequestParam("sort")String sort, @RequestParam("selectType")String selectType, @RequestParam("key")String searchKey, @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "5")int pageSize){
        Map<String,String> map=new HashMap<String,String>();
        map.put("searchKey",searchKey);
        map.put("selectType",selectType);
        map.put("sort",sort);
        PageHelper.startPage(pageNum,pageSize);
        List<Disaster> disasterBySearch = disasterService.findDisasterBySearch(map);
        PageInfo<Disaster> pageInfo=new PageInfo<Disaster>(disasterBySearch);
        if(disasterBySearch!=null){
            return ProcResult.success().bind("pageInfo",pageInfo);
        }
        return ProcResult.fail();
    }

    @ResponseBody
    @RequestMapping("/findAllUserDisaster")
    public ProcResult findAllUserDisaster(@RequestParam("userid")int userid, @RequestParam(value = "key",required = false)String searchKey, @RequestParam(value = "pageNum",required = false,defaultValue = "1")int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Disaster> disaster = disasterService.findAllUserDisaster(userid);
        PageInfo<Disaster> pageInfo=new PageInfo<Disaster>(disaster);
        if(disaster!=null){
            return ProcResult.success().bind("pageInfo",pageInfo);
        }
        return ProcResult.fail();
    }

    @PostMapping("/addDisData")
    @ResponseBody
    public ProcResult addDisData(@RequestParam("tableTitle")String tableTitle,@RequestParam("tableTh")String[] tableTh,@RequestParam("tableTime")String[] tableTime,@RequestParam("tableData")String[] tableData,@RequestParam("disasterid")int disasterid){
        //数据转换
        String th=toSplit(tableTh);
        String time=toSplit(tableTime);
        String data=toSplit(tableData);
        //生成时间，并且封装
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime=sdf.format(date);
        DisData disData=new DisData(disasterid,tableTitle,th,time,data,updateTime);

        int i = disDataService.addData(disData);

        if(i>0){
            //数据转换
            Map<String, Object> map = toMyJson(disData);
            return ProcResult.success().bind("map",map);
        }

        return ProcResult.fail();
    }

    @PostMapping("/updateDisData")
    @ResponseBody
    public ProcResult updateDisData(@RequestParam("tableTitle")String tableTitle,@RequestParam("tableTh")String[] tableTh,@RequestParam("tableTime")String[] tableTime,@RequestParam("tableData")String[] tableData,@RequestParam("tableid")int tableid){
        //数据转换
        String th=toSplit(tableTh);
        String time=toSplit(tableTime);
        String data=toSplit(tableData);
        //生成时间，并且封装
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime=sdf.format(date);
        DisData disData=new DisData(tableid,0,tableTitle,th,time,data,updateTime);
        Map<String, Object> map = toMyJson(disData);
        int i = disDataService.updateDisData(disData);
        if(i>0){
            return ProcResult.success().bind("map",map);
        }
        return ProcResult.fail();
    }

    @ResponseBody
    @GetMapping("/findDisData/{id}")
    public ProcResult findDisData(@PathVariable("id")int disasaterid){
        List<DisData> disDataList = disDataService.findDisData(disasaterid);
        if(disDataList.size()>0){
            List<Map> disDataMap=new ArrayList<Map>();
            for(DisData disData:disDataList){
                disDataMap.add(toMyJson(disData));
            }
            return ProcResult.success().bind("disDataMap",disDataMap);
        }

        return ProcResult.fail();
    }

    @ResponseBody
    @DeleteMapping("/deleteDisData/{id}")
    public ProcResult deleteDisData(@PathVariable("id")int id){
        int i = disDataService.deleteDisData(id);
        if(i>0){
            return ProcResult.success();
        }
        return ProcResult.fail();
    }

    @GetMapping("/jumpDisasterInfo/{id}")
    public String jumpDisasterInfo(Model model, @PathVariable("id") int id){
        boolean isSuccess = disasterService.addHot(id);
        Disaster disaster = disasterService.findDisasterById(id);
        System.out.println(disaster);
        model.addAttribute("disaster",disaster);
        return "userPage/disasterInfo";
    }
    
    @RequestMapping("/jumpAddDisaster")
    public String jumpAddDisaster(){
        return "userPage/addDisaster";
    }

    @RequestMapping("/jumpContactUs")
    public String jumpContactUs(){
        return "userPage/aboutUs";
    }
    @RequestMapping("/jumpMain")
    public String jumpMain(){
        return "userPage/main";
    }

    //自定义数组转化为String
    public String toSplit(String[] data){
        String thList=null;
        thList="";
        for(String tT:data){
            thList+=tT+",";
        }
        return thList;
    }
    //自定义DisData格式转化
    public Map<String,Object> toMyJson(DisData disData){
        Map<String,Object> map=new HashMap<String, Object>();
        String[] splitTh = disData.getTableTh().split("[,|[|]]");
        String[] splitTime = disData.getTableTime().split("[,|[|]]");
        String[] splitData = disData.getTableData().split("[,|[|]]");
        map.put("tableTh",splitTh);
        map.put("tableTime",splitTime);
        map.put("tableData",splitData);
        map.put("tableid",disData.getDdataid());
        map.put("disasterid",disData.getDisasterid());
        map.put("tableTitle",disData.getTableTitle());
        map.put("updateTime",disData.getUpdateTime());

        return map;
    }
}
