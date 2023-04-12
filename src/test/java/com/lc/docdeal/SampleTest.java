package com.lc.docdeal;

import com.lc.docdeal.bean.FileUpload;
import com.lc.docdeal.constant.DocumentConstants;
import com.lc.docdeal.mapper.FileUploadMapper;
import com.lc.docdeal.service.FileUploadService;
import com.lc.docdeal.utils.Md5Utils;
import org.hashids.Hashids;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private FileUploadMapper uploadMapper;
    @Autowired
    private FileUploadService uploadService;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
//        List<User> userList = userMapper.selectList(null);
//        Assert.assertEquals(5, userList.size());
//        userList.forEach(System.out::println);
//        FileUpload upload = new FileUpload();
//        upload.setFile_name("kk.docx");
//        upload.setFile_size(10d);
//        upload.setFile_path("d://upload/kk.docx");
//        upload.setFile_type(".docx");
//        upload.setUpload_date(new Date());
//        int insert = uploadMapper.insert(upload);
//        System.out.println(insert);
        String str = "yxsj2.docx";
        System.out.println(str.substring(str.indexOf(".")));
    }

    public void testFasceSave() {
        try {
            String url = "http://192.168.99.100:9000/coauthoring/CommandService.ashx";
            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("c", "forcesave");
//            String pathShortMd5 = Md5Utils.md5(filePath + name);
//            String nameShortMd5 = Md5Utils.md5(name);
//            Hashids hashids = new Hashids(DocumentConstants.HASH_KEY);
//            // (将路径字符串短md5值 + 名称字符串短md5值) ==> 再转成短id形式 ==> 作为文档的key（暂且认为是不会重复的）
//            String key = hashids.encodeHex(String.format("%s%s", pathShortMd5, nameShortMd5));
//            params.add("key", key);
            params.add("userdata", "sample userdata");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

            JSONObject obj = (JSONObject) new JSONParser().parse(result.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testDeleteTempFile(){
        File file = new File("D:\\doc\\"+"v1sdf.docx");
        if (file.exists()) {
            file.delete();
        }
    }
}
