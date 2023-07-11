function getDailyReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/dailyreport";
}

function displayDailyReportList(data){
    var $tbody = $('#daily-report-table').find('tbody');
    var $download = $('#forDownloadButton');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
    		+ '<td>' + e.date   + '</td>'
    		+ '<td>' + e.invoiced_orders_count + '</td>'
    		+ '<td>' + e.invoiced_items_count  + '</td>'
    		+ '<td>' + e.total_revenue  + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
    	var download = '<button onclick="listDownload()" class="btn btn-info" style="margin-top:1em;><i class="fa fa-download""></i> Download</button>'    	var download = '<button onclick="listDownload()">' + 'Download' + '</button>'
            	$download.empty();
            	if(data.length!=0)
            	$download.append(download);
}

function listDownload() {
var table = document.getElementById("daily-report-table");
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
  link.setAttribute("download", "day_to_day_report.tsv");
  document.body.appendChild(link);

  // Simulate a click event to trigger the download
  link.click();

  // Remove the link element from the DOM
  document.body.removeChild(link);
}

function putValues() {
    var url = getDailyReportUrl();
    var startTime = $('#inputStartDate').val();
    var endTime = $('#inputEndDate').val();
    if(startTime=="") { startTime = "2000-01-01" }
    if(endTime=="") { endTime = "2999-12-12" }
    var form = {
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
                            displayDailyReportList(data);
                	   },
                	   error: handleAjaxError
                	});

}

function init(){
$('#addButton').click(putValues)

}

$(document).ready(init);
