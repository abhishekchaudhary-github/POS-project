function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventoryreport";
}

function displayInventoryReportList(data){
    var $tbody = $('#inventory-report-table').find('tbody');
    var $download = $('#forDownloadButton');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
//SHOULD ID BE THERE ?  !!!!!!!!!
//    		+ '<td>' + e.id   + '</td>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>' + e.quantity + '</td>'
    		+ '<td>' + '</tr>';
            $tbody.append(row);
    	}
    	var download = '<button onclick="listDownload()">' + 'Download' + '</button>'
    	$download.empty();
    	if(data.length!=0)
    	$download.append(download);
}

function listDownload() {
  var element = document.getElementById('inventory-report-table');
  var opt = {
    filename: 'Inventory Report.pdf',
    margin: [10, 10, 10, 10],
    image: { type: 'jpeg', quality: 0.98 },
    html2canvas: { scale: 2 },
    jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
  };

  html2pdf().set(opt).from(element).save();
}



function putValues() {
    var url = getInventoryReportUrl();
    var brand = $('#inputBrand').val();
    var category = $('#inputCategory').val();
    if(brand=="") { brand = null }
    if(category=="") { category = null }
    console.log(brand)
    var form = {
                 brand: brand,
                 category: category
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
                            displayInventoryReportList(data);
                	   },
                	   error: handleAjaxError
                	});

}

function init(){
$('#addButton').click(putValues)

}

$(document).ready(init);
