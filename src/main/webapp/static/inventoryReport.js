var brandArray=[]
var arrayOfBrand=[];
var arrayOfCategory=[];
var brand;
var category;

function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventoryreport";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function displayInventoryReportList(data){
    var $table = $('#inventory-report-table')
    var $download = $('#forDownloadButton');
    	  if ($.fn.DataTable.isDataTable($table)) {
                     $table.DataTable().destroy();
                   }

              // Clear the table body
              $table.find('tbody').empty();
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
//SHOULD ID BE THERE ?  !!!!!!!!!
//    		+ '<td>' + e.id   + '</td>'
    		+ '<td>' + e.brand   + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>' + e.quantity + '</td>'
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
  var table = document.getElementById("inventory-report-table");
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
  link.setAttribute("download", "inventory_report.tsv");
  document.body.appendChild(link);

  // Simulate a click event to trigger the download
  link.click();

  // Remove the link element from the DOM
  document.body.removeChild(link);
}



function putValues() {
    var url = getInventoryReportUrl();
//    var brand = $('#inputBrand').val();
//    var category = $('#inputCategory').val();
    if(brand=="") { brand = null }
    if(category=="") { category = null }

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
//    	console.log(arrayOfBrand)
//    	for(var i=0;i<arrayOfBrand.length;i++) {
//    	    var row = '<button class="dropdown-item" type="button" onclick="changeBrand(' + i + ')">'+arrayOfBrand[i]+'</button>'
//    	    $brandList.append(row);
//    	}

    	var $categoryList = $("#category-dropper");
                    	console.log(arrayOfCategory)
                    	$("#category-dropper").select2({
                    	data:setBrandDropper2});
}


function init(){
$('#addButton').click(putValues)
$("#brand-dropper").on("change", changeBrand);
$("#category-dropper").on("change", changeCategory);
$("#select2-brand-dropper-container").click(changeBrand);
$("#select2-category-dropper-container").click(changeCategory);
}


function changeBrand(id){
           var selectedBrand = $("#brand-dropper").val();
           brand = selectedBrand;
          }

function changeCategory(id){
            var selectedCategory = $("#category-dropper").val();
            category = selectedCategory;
          }

$(document).ready(init);
$(document).ready(getBrandValues);