var selectMonitorNode = {};
var websocket = null;
var monitorFlag = false;
$(function () {
    monitorInit();
    disableButton("monitor", monitor);
    disableButton("unmonitor", unmonitor);
});

function monitorInit() {
    //初始化接口树
    $('#monitorInterTree').tree({
        method: 'get',
        onSelect: function (node) {
            if (monitorFlag) {
                //没有在监听中
                if (selectMonitorNode == node) {
                    //不处理了
                } else {
                    var node = $('#monitorInterTree').tree('find', selectMonitorNode.id);
                    $('#monitorInterTree').tree('select', node.target);
                    $.messager.alert('Warning', '请先停止接口的监听');
                }
            } else {
                selectMonitorNode = node;
                if (node.isLeaf) {
                    //接口
                    disableButton("monitor");
                    enableButton("monitor", monitor);
                } else {
                    disableButton("monitor");
                }
            }
        }
    });
    loadMonitorTree();

    //初始化表单
    $('#monitorUrl').textbox({
        width: 800,
        readonly: true
    });
    $('#monitorBody').textbox({
        multiline: true,
        width: 800,
        readonly: true
    });
    $('#monitorResult').textbox({
        multiline: true,
        width: 800,
        readonly: true
    });


}

function loadMonitorTree() {
    $.get("/list", function (data) {
        $('#monitorInterTree').tree('loadData', data);
    }, "json")
}

function monitor() {
    monitorFlag = true;
    disableButton("monitor");
    disableButton("unmonitor");
    enableButton("unmonitor", unmonitor);
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://" + window.location.host + "/monitor/" + selectMonitorNode.id);
    } else {
        alert('Not support websocket')
    }


    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        $('#monitorReminder').text("监听中");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var result = JSON.parse(event.data);
        $('#monitorUrl').textbox('setValue', result.url);
        $('#monitorBody').textbox('setValue', result.body);
        $('#monitorResult').textbox('setValue', result.result);
        $('#monitorTime').text("接收到请求时间：" + result.time);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        $('#monitorReminder').text("停止监听");
    }


}

function unmonitor() {
    monitorFlag = false;
    $('#monitorTime').text('');
    disableButton("unmonitor");
    disableButton("monitor");
    enableButton("monitor", monitor);
    if (websocket) {
        websocket.close();
    }
}

