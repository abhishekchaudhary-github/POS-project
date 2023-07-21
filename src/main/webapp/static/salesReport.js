var brandArray=[]
var arrayOfBrand=[];
var arrayOfCategory=[];
var brand;
var category;

function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function displaySalesReportList(data){
    var $table = $('#sales-report-table')
    var $download = $('#forDownloadButton');
    if ($.fn.DataTable.isDataTable($table)) {
                         $table.DataTable().destroy();
                       }
    $table.find('tbody').empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>' + e.quantity  + '</td>'
    		+ '<td>' + e.revenue  + '</td>'
    		+ '</tr>';
            $table.find('tbody').append(row);
    	}
    	var download = '<button onclick="listDownload()" class="btn btn-info" style="margin-top:1em;><i class="fa fa-download""></i> Download</button>'
            	$download.empty();
            	if(data.length!=0)
            	$download.append(download);
            	// Initialize DataTable
                                      $table.DataTable({
                                        "paging": false, // Enable pagination
                                        "lengthChange": true, // Hide the page length options
                                        "searching": true, // Enable search functionality
                                        "ordering": false, // Disable sorting
                                        "info": false, // Hide information display
                                        "autoWidth": true, // Disable auto width calculation
                                        "responsive": true // Enable responsive mode
                                      });

                                      if ($("#get-role").text().localeCompare("operator") == 0) {
                                        $(".admin-element").hide();
                                      }
                                      // Apply styles to table header cells
                                        $table.find('th').css('text-align', 'center');
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
//    var brand = $('#inputBrand').val();
//    var category = $('#inputCategory').val();
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

function getBrandValues() {
    var url = getBrandUrl();
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   async:false,
    	   success: function(data) {
    	   		brandArray.push(data);
    	   		for(var i =0;i<brandArray[0].length;i++) {
    	   		    arrayOfBrand.push(brandArray[0][i].brand)
    	   		    arrayOfCategory.push(brandArray[0][i].category)
    	   		}
    	   		//console.log(arrayOfBrand)
    	   },
    	   error: handleAjaxError
    	});

    	var $brandList = $("#brand-dropper");
    	let setBrandDropper = [...new Set(arrayOfBrand)];
                            	$("#brand-dropper").select2({
                            	data:setBrandDropper});

                    let setBrandDropper2 = [...new Set(arrayOfCategory)];

    	var $categoryList = $("#category-dropper");
                    	console.log(arrayOfCategory)
                    	$("#category-dropper").select2({
                    	data:setBrandDropper2});

//    	var $categoryList = $("#category-dropper");
//            	console.log(arrayOfCategory)
//            	for(var i=0;i<arrayOfCategory.length;i++) {
//            	    var row = '<button class="dropdown-item" type="button" onclick="changeCategory(' + i + ')">'+arrayOfCategory[i]+'</button>'
//            	    $categoryList.append(row);
//            	}
}

function changeBrand(id){
           var selectedBrand = $("#brand-dropper").val();
           brand = selectedBrand;
          }

function changeCategory(id){
            var selectedCategory = $("#category-dropper").val();
             category = selectedCategory;
          }


function init(){
$('#addButton').click(putValues)
$("#brand-dropper").on("change", changeBrand);
$("#category-dropper").on("change", changeCategory);
$("#select2-brand-dropper-container").click(changeBrand);
$("#select2-category-dropper-container").click(changeCategory);
}

$(document).ready(init);
$(document).ready(getBrandValues);