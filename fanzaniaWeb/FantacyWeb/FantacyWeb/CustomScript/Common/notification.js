$(document).ready(function () {
    GetNotificationCount();
   // GetNotifications();
});

function GetNotificationCount() {
    $.ajax({
        url: '/Home/GetNotificationCount',
        type: 'POST',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data[0].NotificationCount > 0) {

                $('#NotificationCount').html(data[0].NotificationCount);
                $('.fa-bell').css("font-size", "30px");
            }
            else {
                $('.fa-bell').css("font-size", "14px");
                $('#NotificationCount').html('');
            }

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function GetNotifications() {
    
    $.ajax({
        url: '/Home/GetNotificatons',
        type: 'POST',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.length > 0) {
                var strhtml = ''; var notiCount = 0;
                
                $.each(data, function (index, value) {

                    strhtml += '<li id="li_' + index + '"><div class="details removed-item">';
                    strhtml += '<span class="title">' + value.Title + '</span><span class="descp">' + value.Message + '</span>';
                    strhtml += '<span class="date">' + (value.InsertDate).split('T')[0] + '</span></div>';
                    strhtml += '<button type="button" class="button-default button-dismiss js-dismiss notificationClose" onclick="AckNotification(' + value.NotificationId + ',' + index + ');"><i class="fa fa-close"></i></button></li>';
                    notiCount++;
                });
                var liList = '<li><button style="float: right;" onclick="clearNotification()"; type="button" class="btn btn-danger btn-xs">Clear All</button><div class="details"><span class="title">Top ' + notiCount + ' notifications :</span></div></li>' + strhtml;
                $('#NotificationList').html(liList);
            }
            else {
                strhtml = '<li><button style="float: right;" onclick="clearNotification()"; type="button" class="btn btn-danger btn-xs">Clear All</button><div class="details"><span class="descp">No new notifications available.</span></div></li>';
                $('#NotificationList').html(strhtml);
            }

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

function AckNotification(NotificationId, id) {
    var pData = { NotificationId: NotificationId };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/Home/AckNotificaton',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.status == 'success') {
                GetNotificationCount();
                //.hide("slide", { direction: "left" }, 1000);
                // $("#li_" + id).hide("slide", { direction: "left" }, 400, function () {
                $("#li_" + id).fadeOut(500, function () {
                    $(this).remove();
                });
                GetNotifications();
            }

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}
function clearNotification() {
    var pData = { NotificationId: -1 };
    var jsonData = JSON.stringify(pData);
    $.ajax({
        url: '/Home/AckNotificaton',
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data.status == 'success') {
                GetNotificationCount();
                //.hide("slide", { direction: "left" }, 1000);
                // $("#li_" + id).hide("slide", { direction: "left" }, 400, function () {
              //  $("#li_" + id).fadeOut(500, function () {
                    //$(this).remove();
                //});
                GetNotifications();
            }

        },
        error: function (req, status, error) {
            toastr_warning("Opps! something went wrong. Try reload this page.");
            return false;
        }
    });
}

document.getElementById("NotificationList").addEventListener('click', function (event) {
    event.stopPropagation();
});


