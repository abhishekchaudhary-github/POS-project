function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brandreport";
}

function displayBrandReportList(data){
    var $tbody = $('#brand-report-table').find('tbody');
    var $download = $('#forDownloadButton');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
//SHOULD ID BE THERE ?  !!!!!!!!!
//    		+ '<td>' + e.id   + '</td>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
    	var download = '<button onclick="listDownload()">' + 'Download' + '</button>'
    	$download.empty();
    	if(data.length!=0)
    	$download.append(download);
}

function listDownload() {
  var table = document.getElementById("brand-report-table");
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
  link.setAttribute("download", "brand_report.tsv");
  document.body.appendChild(link);

  // Simulate a click event to trigger the download
  link.click();

  // Remove the link element from the DOM
  document.body.removeChild(link);
}




function putValues() {
    var url = getBrandReportUrl();
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
                            displayBrandReportList(data);
                	   },
                	   error: handleAjaxError
                	});

}

function init(){
$('#addButton').click(putValues)

}

$(document).ready(init);
