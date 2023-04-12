package com.lc.docdeal.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 加密工具类CRC32
 */
@Slf4j
public class CryptoUtils {
    /**
     * CRC32流校验
     *
     * @param input a {@link InputStream} object.
     * @return a {@link String} object.
     */
    public static String crc32(InputStream input) {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkInputStream = null;
        int test = 0;
        try {
            checkInputStream = new CheckedInputStream(input, crc32);
            do {
                test = checkInputStream.read();
            } while (test != -1);
            return Long.toHexString(crc32.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * CRC32文件唯一校验
     *
     * @param file a {@link File} object.
     * @return a {@link String} object.
     */
    public static String crc32(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            return crc32(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * 对文件内容计算crc32校验码
     *
     * @param f 需要计算crc32校验码的文件
     * @return crc校验码
     * @throws IOException 读取文件异常
     */
    public static String file(File f) throws IOException {
        FileInputStream fi = new FileInputStream(f);
        byte[] buff = new byte[64 * 1024];
        int len;
        CRC32 crc32 = new CRC32();
        try {
            while ((len = fi.read(buff)) != -1) {
                crc32.update(buff, 0, len);
            }
        } catch (Exception e) {
            log.error("$$$ 计算crc32出错！");
        } finally {
            fi.close();
        }
        return Long.toHexString(crc32.getValue());
    }
}
