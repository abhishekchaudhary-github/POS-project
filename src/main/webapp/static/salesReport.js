function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function displaySalesReportList(data){
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

//function getSalesReportList(event){
//    	var url = getSalesReportUrl();
//    	$.ajax({
//        	   url: url,
//        	   type: 'GET',
//        	   success: function(data) {
//        	   		displaySalesReportList(data);
//        	   },
//        	   error: handleAjaxError
//        	});
//}

//function toggleBrandClick() {
//            var baseUrl = $("meta[name=baseUrl]").attr("content")
//            var url = baseUrl + "/api/brand";
//        	$.ajax({
//            	   url: url,
//            	   type: 'GET',
//            	   success: function(data) {
//                        var $tbody = $('#dropdown1').find('div');
//                        var url = getSalesReportUrl();
//                        $tbody.empty();
//                        var x = '<a class="dropdown-item dropdown-item-brand" href="#">'Jaction'</a>' +
//                        '<a class="dropdown-item dropdown-item-brand" href="#">'Action'</a>';
//                        $tbody.append(x);
//            	   },
//            	   error: handleAjaxError
//            	});
//}
function putValues() {
    var url = getSalesReportUrl();
    var startTime = $('#inputStartDate').val();
    var endTime = $('#inputEndDate').val();
    var brand = $('#inputBrand').val();
    var category = $('#inputCategory').val();
    if(startTime=="") { startTime = "2000-01-01" }
    if(endTime=="") { endTime = "2999-12-12" }
    if(brand=="") { brand = null }
    if(category=="") { category = null }
    console.log(startTime)
    console.log(brand)
    var form = {
                 brand: brand,
                 category: category,
                 endTime: endTime,
                 startTime: startTime
               }
    $.ajax({
                	   url: url,
                	   type: 'POST',
                	   data: JSON.stringify(form),
                       headers: {
                            'Content-Type': 'application/json'
                       },
                	   success: function(data) {
                	   console.log(data)
                            displaySalesReportList(data);
                	   },
                	   error: handleAjaxError
                	});

}

function init(){
$('#addButton').click(putValues)
//$('.dropdown-item-brand').click(function() {
//      var selectedOption = $(this).text(); // Get the text of the selected option
//      $('.dropdown-toggle-brand').text(selectedOption); // Set the button text to the selected option
//    });
//$('.dropdown-item-category').click(function() {
//      var selectedOption = $(this).text(); // Get the text of the selected option
//      $('.dropdown-toggle-category').text(selectedOption); // Set the button text to the selected option
//    });

}

$(document).ready(init);
//$(document).ready(getSalesReportList);
