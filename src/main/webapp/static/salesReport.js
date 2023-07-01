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
    	var download = '<button onclick="listDownload()">' + 'Download' + '</button>'
    	$tbody.append(download);
}

function listDownload() {
 var doc = new jsPDF();
  var $table = $('#sales-report-table');
  doc.autoTable({
    html: $table.get(0),
    startY: 10
  });
  doc.save("SalesReport.pdf");
}

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

}

$(document).ready(init);
