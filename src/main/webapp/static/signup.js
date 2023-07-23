function init(){
if($("#error-message").text().trim().localeCompare("")!=0){
        let message = $("#error-message").text().trim();
        $.notify(message,{ className:"error" , globalPosition: 'top center'});
    }
    var url = $("meta[name=baseUrl]").attr("content") + "/session/login/message";

    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function() {
    	   		console.log("success")
    	   },
    	   error: handleAjaxError
    	});
}

$(document).ready(init);