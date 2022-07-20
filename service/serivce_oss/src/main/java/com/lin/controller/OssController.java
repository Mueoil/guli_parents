package com.lin.controller;

import com.lin.R;
import com.lin.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {

    @Autowired
    OssService ossService;
//    上传头像的方法
    @PostMapping
    public R uploadOssFile(MultipartFile file){
//        获取上传的文件 MultipartFile
    String url = ossService.uploadFileAvatar(file);
    return R.ok().data("url",url);
    }
}
