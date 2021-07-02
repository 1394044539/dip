package com.wpy.dips.util;

import lombok.Data;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Data
public class UploadPhoto {
    private String savaPath;

    public String savaPhotoPath(MultipartFile multipartFile, String savaPath){
        //当前项目路径
        String nowPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        //另一种取出方法
        // String path = ResourceUtils.getURL("classpath:").getPath();

        File upload = new File(nowPath+savaPath);
        if(!upload.exists()) {//不存在则创建
            upload.mkdirs();
        }
        //拿到文件名并且随机生成一段拼接
        String picName=multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().length()-10,multipartFile.getOriginalFilename().length());
        String mapperName = UUID.randomUUID().toString().substring(0, 10);
        //创建新文件
        File file=new File(upload,mapperName+picName);
        try {//将图片存入
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String photoPath="/"+savaPath+file.getName();
        return photoPath;

    }
}
