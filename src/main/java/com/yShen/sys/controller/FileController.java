package com.yShen.sys.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.yShen.sys.constast.SysConstast;
import com.yShen.sys.util.AppFileUtils;
import com.yShen.sys.util.DataGridView;
import com.yShen.sys.util.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载的控制器
 */
@Controller
@RequestMapping("file")
public class FileController {

    /**
     * 添加
     */
    @RequestMapping("uploadFile")
    @ResponseBody
    public DataGridView uploadFile(MultipartFile mf) throws IllegalStateException, IOException {
        // 文件上传的父目录
        String parentPath = AppFileUtils.PATH;
        // 得到当前日期作为文件夹名称
        String dirName = RandomUtils.getCurrentDateForString();
        // 构造文件夹对象
        File dirFile = new File(parentPath, dirName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();// 创建文件夹
        }
        // 得到文件原名
        String oldName = mf.getOriginalFilename();
        // 根据文件原名得到新名
        String newName = RandomUtils.createFileNameUseTime(oldName, SysConstast.FILE_UPLOAD_TEMP);
        File dest = new File(dirFile, newName);
        mf.transferTo(dest);
        Map<String, Object> map = new HashMap<>();
        map.put("src", dirName + "/" + newName);
        System.out.println(map.get("src"));
        DataGridView dataGridView = new DataGridView();
        dataGridView.setData(map);
        return dataGridView;

    }

    /**
     * 不下载只显示
     */
    @RequestMapping("downloadShowFile")
    public ResponseEntity<Object> downloadShowFile(String path, HttpServletResponse response) {
        return AppFileUtils.downloadFile(response, path, "");
    }

    /**
     * 下载图片
     */
    @RequestMapping("downloadFile")
    public ResponseEntity<Object> downloadFile(String path, HttpServletResponse response) {
        String oldName = "";
        return AppFileUtils.downloadFile(response, path, oldName);
    }

}
