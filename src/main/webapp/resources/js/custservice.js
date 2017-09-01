function checkAndCloseBrowser() {
    var browserName = navigator.appName;
    var browserVer = parseInt(navigator.appVersion);
//    alert(browserName + " : " + browserVer);

    //document.getElementById("flashContent").innerHTML = "<br>&nbsp;<font face='Arial' color='blue' size='2'><b> You have been logged out of the Game. Please Close Your Browser Window.</b></font>";

    if (browserName === "Microsoft Internet Explorer") {
//        alert('within IE');
        var ie7 = (document.all && !window.opera && window.XMLHttpRequest) ? true : false;
        if (ie7)
        {
            //This method is required to close a window without any prompt for IE7 & greater versions.
            window.open('', '_self', '');
            window.close();
        }
        else
        {
            //This method is required to close a window without any prompt for IE6
            this.focus();
//            self.opener = this;
            this.close();
        }
    } else {
        //For NON-IE Browsers except Firefox which doesnt support Auto Close
        try {
            this.focus();
            this.close();
            window.open('', '_self', '');
            window.close();

        }
        catch (e) {
        }
    }
    try {
        window.open('', '_self', '');
        window.close();
    }
    catch (e) {
    }

}

function emailValidate(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === 64 || charCode === 46 || (charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))
            return true;
    }
    return false;

}
function charsAndNumbers(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === 190 || (charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))
            return true;
    }
    return false;

}

function charsAndNumbersAndSpace(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === 32 || charCode === 190 || (charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))
            return true;
    }
    return false;

}

function numbersOnly(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === 8 || charCode === 190)
            return true;
        if ((charCode < 48 || charCode > 57) || (charCode === 187 || charCode === 192))
            return false;
    }
    return true;
}

function telephoneNo(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === 8 || charCode === 189 || charCode === 191)
            return true;
        if ((charCode < 45 || charCode > 57) || (charCode === 187 || charCode === 192 || charCode === 20))
            return false;
    }
    return true;
}
function charsAndNumbersAndAt(event) {
    if (event) {
        var charCode = (event.which) ? event.which : event.keyCode;
        if (charCode === parseInt("@") || charCode === 190 || (charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))
            return true;
    }
    return false;

}
function disableRightClick() {

    jQuery(document).ready().on({
        "contextmenu": function (e) {
            e.preventDefault();
        },
        "mousedown": function (e) {
        },
        "mouseup": function (e) {
        }
    });
    jQuery(document).unbind('keydown').bind('keydown', function (event) {
        var doPrevent = false;
        if (event.keyCode === 8) {
            var d = event.srcElement || event.target;
            if ((d.tagName.toUpperCase() === 'INPUT' && (d.type.toUpperCase() === 'TEXT' || d.type.toUpperCase() === 'PASSWORD'))
                    || d.tagName.toUpperCase() === 'TEXTAREA') {
                doPrevent = d.readOnly || d.disabled;
            } else {
                doPrevent = true;
            }
        }
        if (event.keyCode === 17) {
            event.preventDefault();
        }

        if (doPrevent) {
            event.preventDefault();
        }

    });
    jQuery(document).ready().on({
        "contextmenu": function (e) {
            e.preventDefault();
        },
        "mousedown": function (e) {
        },
        "mouseup": function (e) {
        }
    });

}


function toggleContactsPanel() {
    jQuery('#contactspanel').animate({width: ['toggle', 'swing'], height: ['toggle', 'swing'], opacity: 'toggle'}, 900);
}

function toggleloginpanel() {
    jQuery('#loginfrm\\:loginpanel').show();
}
