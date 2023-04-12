var Editor = function () {
    var docEditor;

    var innerAlert = function (message) {
        if (console && console.log)
            console.log(message);
    };

    var onAppReady = function () {
        innerAlert("文档编辑已就绪~");
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
        //TODO
        location.reload(true);
    };
    var onAppReady = function() {
        console.log("ONLYOFFICE Document Editor is ready");
    };
    var onCollaborativeChanges = function () {
        console.log("The document changed by collaborative user");
    };
    var onDocumentReady = function() {
        console.log("Document is loaded");
    };
    var onDocumentStateChange = function (event) {
        if (event.data) {
            console.log("The document changed");
            // docEditor.downloadAs();
        } else {
            console.log("Changes are collected on document editing service");
            //
        }
    };
    var onDownloadAs = function (event) {
        console.log("ONLYOFFICE Document Editor create file: " + event.data);
        window.top.postMessage(event.data);
        createAndDownloadFile("test.docx",event.data)
    };
    window.addEventListener('message',function(e){
        console.log(e.data)
        if (e.data=="downloadAs") {
            docEditor.downloadAs();
        }
    },false)

    $("#insertImage").click(function(event) {
        console.log("ONLYOFFICE Document Editor insertImage: "+ event.data);
        docEditor.insertImage({
            "fileType": "png",
            "url": "http://192.168.0.58:20056/attachment/20190728测试上传文件名修改/2020January/1580363537940306800_small.png"
        });
    })

    var onRequestInsertImage = function(event) {
        console.log("ONLYOFFICE Document Editor insertImage" + event.data);
        docEditor.insertImage({
            "fileType": "png",
            "url": "http://192.168.0.58:20056/attachment/20190728测试上传文件名修改/2020January/1580363537940306800_small.png"
        });
    };

    var onError = function (event) {
        console.log("ONLYOFFICE Document Editor reports an error: code " + event.data.errorCode + ", description " + event.data.errorDescription);
    };
    var onOutdatedVersion = function () {
        location.reload(true);
    };
    var onRequestEditRights = function () {
        console.log("ONLYOFFICE Document Editor requests editing rights");
        // document.location.reload();
        var he=location.href.replace("view","edit");
        location.href=he;
    };

    //历史版本保留1个月。比如Unix时间戳（Unix timestamp）expires=1524547423
    var onRequestHistory = function() {
    };

    var onRequestHistoryClose = function() {
        document.location.reload();
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
    var onRequestHistoryData = function() {}
    var сonnectEditor = function (document, documentEditParam) {

        var config = getDocumentConfig(document);
        console.log(document)
        console.log(documentEditParam)
        config.width = "100%";
        config.height = "100%";
        config.events = {
            "onAppReady": onAppReady,
            "onDocumentStateChange": onDocumentStateChange,
            "onError": onError,
            "onOutdatedVersion": onOutdatedVersion
        };
        //config.documentType = ""+document.fileType
        config.editorConfig = {
            "lang": "zh-CN",
            "mode": "edit",
            "recent": [],
            // 自定义一些配置
            "customization": {
                "chat": true, // 禁用聊天菜单按钮
                "commentAuthorOnly": false, // 仅能编辑和删除其注释
                "comments": false, // 隐藏文档注释菜单按钮
                "compactHeader": false, // 隐藏附加操作按钮
                "compactToolbar": false, // 完整工具栏(true代表紧凑工具栏)
                "feedback": {
                    "visible": true // 隐藏反馈按钮
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
            },




        };
        $.extend(config.editorConfig, documentEditParam);
        console.log(config)


        docEditor = new DocsAPI.DocEditor("iframeEditor",config);
    };

    return {
        init: function (document, documentEditParam) {
            сonnectEditor(document, documentEditParam);
        }
    }
}();
