function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function displaySalesReportList(data){
    var $tbody = $('#sales-report-table').find('tbody');
    var $download = $('#forDownloadButton');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>' + e.quantity  + '</td>'
    		+ '<td>' + e.revenue  + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
    	var download = '<button onclick="listDownload()" class="btn btn-info" style="margin-top:1em;><i class="fa fa-download""></i> Download</button>'
            	$download.empty();
            	if(data.length!=0)
            	$download.append(download);
}

function listDownload() {
var table = document.getElementById("sales-report-table");
  var headerRow = table.getElementsByTagName("thead")[0].getElementsByTagName("tr")[0];
  var rows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");

  var tsvContent = "";
  var headerCells = headerRow.getElementsByTagName("th");

  // Add table header to TSV content
  for (var i = 0; i < headerCells.length; i++) {
    tsvContent += headerCells[i].innerText;
    if (i < headerCells.length - 1) {
      tsvContent += "\t"; // Use tab as the separator
    }
  }
  tsvContent += "\n"; // Add a new line after the header

  // Add table rows to TSV content
  for (var i = 0; i < rows.length; i++) {
    var cells = rows[i].getElementsByTagName("td");
    for (var j = 0; j < cells.length; j++) {
      tsvContent += cells[j].innerText;
      if (j < cells.length - 1) {
        tsvContent += "\t"; // Use tab as the separator
      }
    }
    tsvContent += "\n"; // Add a new line after each row
  }

  // Create a hidden link element
  var link = document.createElement("a");
  link.setAttribute("href", "data:text/tsv;charset=utf-8," + encodeURIComponent(tsvContent));
  link.setAttribute("download", "sales_report.tsv");
  document.body.appendChild(link);

  // Simulate a click event to trigger the download
  link.click();

  // Remove the link element from the DOM
  document.body.removeChild(link);
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
