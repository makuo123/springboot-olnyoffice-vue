package com.lc.docdeal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.docdeal.bean.FileUpload;

import java.util.List;

public interface FileUploadService extends IService<FileUpload> {


    List<FileUpload> list();


}
