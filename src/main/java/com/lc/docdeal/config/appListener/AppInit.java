package com.lc.docdeal.config.appListener;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Component
public class AppInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

        //创建文件夹
        File file = new File("sqlite");
        if (!file.exists()){
            file.mkdir();
            //创建数据库文件，并创建表
            String sql="jdbc:sqlite:sqlite/file_info.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(sql);
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists file_info");
            stat.executeUpdate("CREATE TABLE `file_info` (\n" +
                    "                `id` integer NOT NULL primary key autoincrement,\n" +
                    "        `file_name` varchar(50) NOT NULL ,\n" +
                    "                `file_size` bigint DEFAULT NULL ,\n" +
                    "                `file_type` varchar(20) DEFAULT NULL ,\n" +
                    "                `file_path` varchar(50) NOT NULL,\n" +
                    "                `upload_date` date DEFAULT NULL)");
        }else {

        }

    }
}
