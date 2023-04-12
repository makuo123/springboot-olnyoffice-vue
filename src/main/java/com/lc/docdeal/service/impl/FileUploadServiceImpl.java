package com.lc.docdeal.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.docdeal.bean.FileUpload;
import com.lc.docdeal.mapper.FileUploadMapper;
import com.lc.docdeal.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload> implements FileUploadService {
    @Autowired
    FileUploadMapper  uploadMapper;

    @Override
    public List<FileUpload> list() {
        return uploadMapper.selectList(null);
    }


}
