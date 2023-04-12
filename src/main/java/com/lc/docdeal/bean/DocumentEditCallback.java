package com.lc.docdeal.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文档编辑时的回调实体
 *
 * <p>
 * 每个用户与文档共同编辑的连接或断开都接收状态 1 。
 * 文档关闭后10秒钟收到状态 2 （ 3 ），其中包含最后一次将更改发送到文档编辑服务的用户的id。
 * 在文档关闭进行编辑后收到状态 4 ，最后一个用户不做任何更改。
 * 当执行强制保存请求时，接收状态 6 （ 7 ）。
 * </p>
 * @author: zhangcx
 * @date: 2019/8/23 13:48
 */
@Data
public class DocumentEditCallback implements Serializable {
    /** 连接到文档的新用户共同编辑或与其断开连接的操作对象信息 */
    private List<ActionsBean> actions;
    /** 用于跟踪和显示文档更改历史记录的文档编辑数据 链接 */
    private String changesurl;
    /** 执行强制保存请求时 启动器的类型 */
    /**
     0 - 对命令服务执行强制保存请求，
     1 - 每次保存时执行强制保存请求（例如，单击“ 保存”按钮），仅当forcesave选项设置为true时才可用。
     2 - 使用服务器配置中的设置由计时器执行强制保存请求。
     当状态值仅等于6或7时，存在类型。
     */
    private int forcesavetype;
    /** 文档更改历史记录 */
    private HistoryBean history;
    /** 【必需】文档key */
    private String key;
    /**
     *  【必需】文档的状态 {@link DocumentStatus}
     *  <ul>
     *  <li>每个用户与文档共同编辑的连接或断开都接收状态 1</li>
     *  <li>文档关闭后10秒钟收到状态 2 （ 3 ），其中包含最后一次将更改发送到文档编辑服务的用户的id</li>
     *  <li>最后一个用户不做任何更改，在文档关闭后收到状态 4 </li>
     *  <li>当执行强制保存请求时，接收状态 6 （ 7 ）</li>
     *  </ul>
     */
    private int status;
    /** 文档存储服务中缓存的已编辑的文档的链接 */
    private String url;
    /** 发送到命令服务的自定义信息 */
    private String userdata;
    /** 同时打开文档进行编辑的用户id列表; 当文档被更改保存时，用户将返回最后一个编辑文档的用户id（状态2和状态6时） */
    private List<String> users;

    /**
     * 文档更改历史记录Bean
     */
    @Data
    public static class HistoryBean {
        /**
         * 修改记录（json字符串形式）
         <pre>
         [{
                "created": "2019-08-28 05:25:23",
                "user": {
                    "name": "用户456",
                    "id": "456"
                }
            }, {
                "created": "2019-08-28 05:25:33",
                "user": {
                    "name": "用户789",
                    "id": "789"
                }
            }
         ]
         </pre>
         */
        private String changes;
        /** onlyoffice服务端版本 **/
        private String serverVersion;
    }

    /**
     * 连接到文档的新用户共同编辑或与其断开连接的操作对象信息Bean
     */
    @Data
    public static class ActionsBean {
        /** 1:建立编辑连接 0：断开连接 */
        private int type;
        /** 共同编辑文档时 连接或断开连接的用户id */
        private String userid;
    }
}
