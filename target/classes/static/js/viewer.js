var Viewer = function() {
    var docEditor;

    var innerAlert = function (message) {
        if (console && console.log)
            console.log(message);
    };

    var onAppReady = function () {
        innerAlert("文档查看已就绪~");
    };

    var onDocumentStateChange = function (event) {
        var title = document.title.replace(/\*$/g, "");
        document.title = title + (event.data ? "*" : "");
    };

    var onError = function (event) {
        if (event) {
            innerAlert(event.data);
        }
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

    var getDocumentConfig = function (document) {
        if (document) {
            return {
                "document": document
            };
        }
        innerAlert("文档未指定！");
        return null;
    };

    var сonnectEditor = function (document) {
        var config = getDocumentConfig(document);
        config.width = "100%";
        config.height = "100%";
        config.events = {
            "onAppReady": onAppReady,
            "onDocumentStateChange": onDocumentStateChange,
            "onError": onError,
            "onOutdatedVersion": onOutdatedVersion
        };
        config.editorConfig = {
            "lang": "zh-CN",
            "mode": "view",
            "recent": [],
            // 自定义一些配置
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
        docEditor = new DocsAPI.DocEditor("iframeEditor", config);
    };

    return {
        init : function(document) {
            сonnectEditor(document);
        }
    }
}();