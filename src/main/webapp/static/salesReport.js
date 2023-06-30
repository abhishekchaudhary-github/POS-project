function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function displaySalesReportList(){
    var $tbody = $('#sales-report-table').find('tbody');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>' + e.quantity  + '</td>'
    		+ '<td>' + e.revenue  + '</td>'
    		+ '<td>' + '</tr>';
            $tbody.append(row);
    	}
}

function getSalesReportList(event){
    	var url = getSalesReportUrl();
    	$.ajax({
        	   url: url,
        	   type: 'GET',
        	   success: function(data) {
        	   		displaySalesReportList(data);
        	   },
        	   error: handleAjaxError
        	});
}




function init(){
$('.dropdown-item-brand').click(function() {
      var selectedOption = $(this).text(); // Get the text of the selected option
      $('.dropdown-toggle-brand').text(selectedOption); // Set the button text to the selected option
    });
$('.dropdown-item-category').click(function() {
      var selectedOption = $(this).text(); // Get the text of the selected option
      $('.dropdown-toggle-category').text(selectedOption); // Set the button text to the selected option
    });
}

$(document).ready(init);
$(document).ready(getSalesReportList);
