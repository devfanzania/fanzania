function requiredField(input) {
    
    if (input.value.length < 1) {
        //red border
        input.style.borderColor = "#e74c3c";

    } else if (input.type == "email") {
        //console.log("this is an email type");

        if (input.value.indexOf("@") != -1 && input.value.indexOf(".") != -1) {
            //green border
            input.style.borderColor = "#2ecc71";
            document.getElementById("EmailErrMsg").innerHTML = "";
        } else {
            //red border
            input.style.borderColor = "#e74c3c";
            document.getElementById("EmailErrMsg").innerHTML = "** Please enter a proper email id";
        }

    } else {
        //green border
        input.style.borderColor = "#2ecc71";
    }
}

function OpenDailog() {
    document.getElementById("txtEmailForgot").innerHTML = '';
    $("#ModalForgotPassword").modal('show');
}

function ForgetPass() {
   
    $("#ModalForgotPassword").modal('hide');
    email = $('#txtEmailForgot').val();
    var pData = { Email: email };
    var jsonData = JSON.stringify(pData);
    var uurl = '/Account/ForgotPassword';
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {

            alert(data.statusMessage);
        },
        error: function (req, status, error) {
            alert('Unable to connect server!');
            return false;
        }
    });
}


function Logout() {
    //alert('logout');
    sessionStorage.clear();
    location.href = '/Account/Logout';
}

function AdminLogout() {
    //alert('logout');
    sessionStorage.clear();
    location.href = '/Admin/Logout';
}

function isValidEmailAddress(emailAddress) {
   // alert(emailAddress);
    var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(emailAddress);
}

$('#btn_google').click(function () {
    $(this).data('clicked',true);
});
function onSignIn(googleUser) {
   
    if ($('#btn_google').data('clicked')) {
     
        var profile = googleUser.getBasicProfile();
        //console.log(profile);
        //console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
        //console.log('Name: ' + profile.getName());
        //console.log('Image URL: ' + profile.getImageUrl());
        //console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
        var email = profile.getEmail();
        var id = profile.getId();
        var name = profile.getName();
        ExternalLoginValidateGoogle(email, id, name);
    }
    
}


function ExternalLoginValidateGoogle(email, id, name) {
   // console.log($("#getlat").val()); 
    var lat = $("#getlat").val();
    var long = $("#getlong").val();
    var uurl = '/Account/ExternalLoginValidateGoogle';
    var pData = { Email: email, ExternalUserID: id, Name: name, Lat: lat, Long:long};
    //console.log(pData);
    var jsonData = JSON.stringify(pData);
    //console.log(jsonData);
    $.ajax({
        url: uurl,
        type: 'POST',
        data: jsonData,
        dataType: 'json',
        async: true,
        cache: false,
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            if (data == 'success') {
                location.href = '/Dashboard/Index';
            }
            else {
                toastr_warning(data);
            }
        },
        error: function (req, status, error) {
            toastr_warning(error);
            
            return false;
        }
    });

}



