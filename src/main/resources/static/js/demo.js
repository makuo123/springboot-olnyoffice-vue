$(function () {
    var docEditor;

    var innerAlert = function (message) {
        if (console && console.log)
            console.log(message);
    };

    var onAppReady = function () {
        innerAlert("文档已就绪~");
    };

    var onDocumentStateChange = function (event) {
        var title = document.title.replace(/\*$/g, "");
        document.title = title + (event.data ? "*" : "");
    };

    var onRequestEditRights = function () {
        location.href = location.href.replace(RegExp("mode=view\&?", "i"), "");
    };

    var onError = function (event) {
        if (event)
            innerAlert(event.data);
    };

    var onOutdatedVersion = function (event) {
        location.reload(true);
    };

    var getUrlParam = function (name) {
        //构造一个含有目标参数的正则表达式对象
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        //匹配目标参数
        var r = window.location.search.substr(1).match(reg);
        //返回参数值
        if (r != null) {
            return decodeURI(r[2]);
        }
        return null;
    };

    var getDocument = function () {
        // 文件名（此处先从连接参数中获取，方便调试）
        var fileName = getUrlParam('file');
        //console.log(fileName);
        !fileName && (fileName = "doc.doc");
        //console.log(fileName);

        // 文件类型（扩展名）
        var ext = fileName.substr(fileName.lastIndexOf(".") + 1);
        return {
            "document": {
                "fileType": ext,
                "key": "sherlocky_oss_office_" + new Date().getMilliseconds() + "_" + fileName, // 不同文件key必须唯一
                "title": "测试文档" + fileName,
                "url": "http://192.168.0.58:20053//download"
            }
        };
    };

    var сonnectEditor = function () {
        var config = getDocument();
        config.width = "100%";
        config.height = "100%";
        config.events = {
            "onAppReady": onAppReady,
            "onDocumentStateChange": onDocumentStateChange,
            //'onRequestEditRights': onRequestEditRights,
            "onError": onError,
            "onOutdatedVersion": onOutdatedVersion
        };

        config.editorConfig = {
            //"actionLink": ACTION_DATA,
            // edit时必须的字段
            "callbackUrl": "http://10.4.89.60:8080/api/callback",
            //"createUrl": "https://example.com/url-to-create-document/",
            "lang": "zh-CN",//"en-US",
            "mode": "view",//"edit",
            "recent": [
                /**
                 {
                            "folder": "Example Files",
                            "title": "exampledocument1.docx",
                            "url": "https://example.com/exampledocument1.docx"
                        }
                 */
            ],
            // maybe可定制各个学校的用户
            "user": {
                "id": "123456",
                "name": "Sherlock"
            }
            ,
            // 可自定义一些配置
            "customization": {
                "chat": false, // 禁用聊天菜单按钮
                "commentAuthorOnly": true, // 仅能编辑和删除其注释
                "comments": false, // 隐藏文档注释菜单按钮
                "compactHeader": false, // 隐藏附加操作按钮
                "compactToolbar": false, // 完整工具栏(true代表紧凑工具栏)
                "feedback": {
                    "visible": false // 隐藏反馈按钮
                },
                "forcesave": false, // true 表示强制文件保存请求添加到回调处理程序
                "goback": false,/*{
                            "blank": true, // 转到文档时，在新窗口打开网站(false表示当前窗口打开)
                            "text": "转到文档位置（可以考虑放文档打开源页面）",
                            // 文档打开失败时的跳转也是该地址
                            "url": "http://www.lezhixing.com.cn"
                        },*/
                "help": false, // 隐藏帮助按钮
                "hideRightMenu": false, // 首次加载时隐藏右侧菜单(true 为显示)
                "showReviewChanges": false, // 加载编辑器时自动显示/隐藏审阅更改面板(true显示 false隐藏)
                "toolbarNoTabs": false, // 清楚地显示顶部工具栏选项卡(true 代表仅突出显示以查看选择了哪一个)
                "zoom": 100 // 定义文档显示缩放百分比值
            }

        };

        //config.editorConfig.customization = {};

        docEditor = new DocsAPI.DocEditor("iframeEditor", config);
    };

    сonnectEditor();
});
