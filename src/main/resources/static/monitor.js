var selectMonitorNode = {};
var websocket = null;
$(function () {
    monitorInit();
});

function monitorInit() {
    //初始化接口树
    $('#monitorInterTree').tree({
        method: 'get',
        onSelect: function (node) {
            selectMonitorNode = node;
            if (node.isLeaf) {
                //接口
                enableButton("monitor", monitor);
            }
        }
    });
    loadMonitorTree();

    //初始化表单
    $('#monitorUrl').textbox({
        width: 800
    });
    $('#monitorBody').textbox({
        multiline: true,
        width: 800
    });
    $('#monitorResult').textbox({
        multiline: true,
        width: 800
    });


}

function loadMonitorTree() {
    $.get("/list", function (data) {
        $('#monitorInterTree').tree('loadData', data);
    }, "json")
}

function monitor() {
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://" + window.location.host + "/monitor/" + selectMonitorNode.id);
    } else {
        alert('Not support websocket')
    }
    enableButton("unmonitor", unmonitor);
}

function unmonitor() {
    if (websocket) {
        websocket.close();
    }
}