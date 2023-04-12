package com.lc.docdeal.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;

public class FileUtil {
    /**
     * 保存文件方法
     * @param in
     * @param outPath
     * @throws Exception
     */
    public static void saveFile(InputStream in, String outPath) throws Exception {
//        FileChannel in = new FileInputStream("src/demo20/data.txt").getChannel(),
//                out = new FileOutputStream("src/demo20/data2.txt").getChannel();
//        in.transferTo(0, in.size(), out);
        OutputStream osm = new FileOutputStream(outPath);
        IOUtils.copy(in, osm);

    }

    /**
     * 前端下载文件方法
     * @param name
     * @param filePath
     * @param response
     * @throws IOException
     */
    public static void downLoadFile(String name, String filePath, HttpServletResponse response) throws IOException {

        String path = filePath + name;
        // path是指想要下载的文件的路径
        File file = new File(path);
        //log.info(file.getPath());
        // 获取文件名
        String filename = file.getName();
        // 获取文件后缀名
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        System.out.println("文件后缀名：" + ext);

        // 将文件写入输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream fis = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * 编辑以后保存文件
     * @param jsonObj
     * @param filePath
     * @param request
     * @param response
     * @throws IOException
     */
    public static void callBackSaveDocument(JSONObject jsonObj, String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
         * 当我们关闭编辑窗口后，十秒钟左右onlyoffice会将它存储的我们的编辑后的文件，，此时status = 2，通过request发给我们，我们需要做的就是接收到文件然后回写该文件。
         * */
        /*
         * 定义要与文档存储服务保存的编辑文档的链接。当状态值仅等于2或3时，存在链路。
         * */
        String downloadUri = (String) jsonObj.get("url");
        System.out.println("====文档编辑完成，现在开始保存编辑后的文档，其下载地址为:" + downloadUri);
        //解析得出文件名
        //String fileName = downloadUri.substring(downloadUri.lastIndexOf('/')+1);
        String fileName = request.getParameter("fileName");
        System.out.println("====下载的文件名:" + fileName);

        URL url = new URL(downloadUri);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();
        //更换为实际的路径F:\DataOfHongQuanzheng\java\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Java Example\\app_data\192.168.56.1\
        //File savedFile = new File("F:\\DataOfHongQuanzheng\\onlyoffice_data\\app_data\\"+fileName);
        File savedFile = new File(filePath + fileName);
        if (null!=((String) jsonObj.get("userdata"))&&((String) jsonObj.get("userdata")).equals("sample userdata")) {
            savedFile = new File(filePath + "v1" + fileName);
        }


        try (FileOutputStream out = new FileOutputStream(savedFile)) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        }
        connection.disconnect();
    }

    /**
     * 发送网路请求查看是否正在编辑
     * @param path
     * @param params
     * @return
     */
    public static String editStatus(String path, String params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
//            String jsonStr = "{\"c\":\"forcesave\", \"key\":\"WpP7m85eNQSEOoepp31oIYVG2oJyJJcvkLdoywgvs1k3ywm3Omuxk4\",\"userdata\":\"sample userdata\"}";
            out.write(params);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {

                return IOUtils.toString(conn.getInputStream());


            } else {
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 当最后关闭编辑界面后，将编辑时下载的文件删除
     *
     * @param path
     * @param fileName
     */
    public static void deleteTempFile(String path, String fileName) {
        //因为临时存储的文件都添加了v1前缀所以删除文件时需要在文件名测前边加一个v1
        File file = new File(path + "v1" + fileName);
        if (file.exists()) {
            file.delete();
        }

    }
}
