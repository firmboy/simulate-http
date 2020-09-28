var addNode = {};
var selectNode = {};
$(function () {
    init();
    button();
});

//按钮事件绑定
function button() {
    //增加分组
    $('#addGroup').bind('click', function () {
        var node = $('#interTree').tree('getSelected');
        addNode = {};
        addNode.id = guid();
        addNode.isLeaf = false;
        addNode.children = [];
        if (node) {
            if (node.isLeaf) {
                $.messager.alert('Warning', '分组不能添加在接口下');
                return;
            }
            addNode.pId = node.id;
        }
        $("#interAdd").dialog('open');
    });
    //增加节点
    $('#addInterface').bind('click', function () {
        var node = $('#interTree').tree('getSelected');
        if (node) {
            if (node.isLeaf) {
                $.messager.alert('Warning', '接口不能添加下级');
                return;
            }
            addNode = {};
            addNode.id = guid();
            addNode.isLeaf = true;
            addNode.pId = node.id;
            $("#interAdd").dialog('open');
        } else {
            $.messager.alert('Warning', '请选择分组');
        }
    });

    $('#delete').bind('click', function () {
        $.messager.confirm('删除', "确定要删除吗", function (r) {
            if (r) {
                $.ajax("delete", {
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(selectNode)
                }).done(function (data) {
                    loadTree();
                });
            }
        })
    })
}

//注册接口页签初始化
function init() {
    //初始化接口树
    $('#interTree').tree({
        method: 'get',
        onSelect: function (node) {
            if (node) {
                selectNode = node;
                if (node.isLeaf) {
                    if (node.hasRegis) {
                        disableButton("registered");
                        disableButton("unregistered");
                        enableButton("unregistered", unregistered);
                    } else {
                        disableButton("unregistered");
                        disableButton("registered");
                        enableButton("registered", registered);
                    }
                    $('#reminder').text("接口");
                    $('#interAddr').textbox('setValue', node.addr);
                    $('#interResultType').textbox('setValue', node.resultType);
                    if (node.addr) {
                        $('#interResultType').textbox('disable');
                    } else {
                        $('#interResultType').textbox('enable');
                    }
                    $('#interResult').textbox('setValue', node.result);
                    $('#interFormName').textbox('setValue', node.text);
                } else {
                    disableButton("registered");
                    disableButton("unregistered");
                    $('#reminder').text("分组");
                    $('#interAddr').textbox('setValue', "");
                    $('#interResultType').textbox('setValue', "json");
                    $('#interResult').textbox('setValue', "");
                    $('#interFormName').textbox('setValue', node.result);
                }
            }
        },
        onLoadSuccess: function (node, data) {
            if (selectNode.id) {
                var node = $('#interTree').tree('find', selectNode.id);
                $('#interTree').tree('select', node.target);
            }
        }
    });
    loadTree();

    //初始化按钮
    $('#addGroup').linkbutton({
        iconCls: 'icon-add'
    });
    $('#addInterface').linkbutton({
        iconCls: 'icon-add'
    });
    $('#delete').linkbutton({
        iconCls: 'icon-remove'
    });

    //初始化表单
    $('#interAddr').textbox({
        width: 800
    });
    $('#interResult').textbox({
        multiline: true,
        width: 800
    });
    $('#interFormName').textbox({
        width: 800
    })

    $('#interAdd').dialog({
        title: '新增',
        width: 400,
        height: 150,
        closed: true,
        closable: false,
        buttons: [{
            text: '确定',
            handler: function () {
                if ($('#interName').textbox('getValue')) {
                    addNode.text = $('#interName').textbox('getValue');
                    $('#interName').textbox('clear');
                    $.ajax("saveInter", {
                        method: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(addNode)
                    }).done(function (data) {
                        selectNode = addNode;
                        loadTree();
                    });
                    $("#interAdd").dialog('close');
                } else {
                    $.messager.alert('Warning', '名称必填');
                }

            }
        }, {
            text: '取消',
            handler: function () {
                $("#interAdd").dialog('close');
            }
        }]
    });
    $('#interName').textbox({
        width: 200
    });
    disableButton("registered");
    disableButton("unregistered");
}

function loadTree() {
    $.get("/list", function (data) {
        $('#interTree').tree('loadData', data);
    }, "json")
}

function registered() {
    var interFormName = $('#interFormName').textbox('getValue');
    var interAddr = $('#interAddr').textbox('getValue');
    var interResult = $('#interResult').textbox('getValue');
    var interResultType = $('#interResultType').textbox('getValue');
    if (interFormName && interAddr && interResult) {
        selectNode.addr = interAddr;
        selectNode.result = interResult;
        selectNode.resultType = interResultType;
        selectNode.text = interFormName;
        selectNode.hasRegis = true;
        $.ajax("registered", {
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(selectNode)
        }).done(function (data) {
            if (data.flag == "success") {
                $.messager.alert('结果', '注册成功');
                loadTree();
            } else {
                $.messager.alert('结果', data.msg);
            }

        });
    } else {
        $.messager.alert('Warning', '地址和返回值必填');
    }
}

//取消注册
function unregistered() {
    selectNode.hasRegis = false;
    $.ajax("unregistered", {
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(selectNode)
    }).done(function (data) {
        if (data.flag == "success") {
            $.messager.alert('结果', '取消注册成功');
            loadTree();
        } else {
            $.messager.alert('结果', data.msg);
        }

    });
}
