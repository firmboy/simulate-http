function disableButton(id) {
    $('#' + id).linkbutton('disable');
    $('#' + id).unbind('click')
}

function enableButton(id, Func) {
    $('#' + id).linkbutton('enable');
    $('#' + id).bind('click', Func);
}


function guid() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }

    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}