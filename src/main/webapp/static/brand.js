function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var brandField = $form.find('#inputBrand').val().trim()
	var categoryField = $form.find('#inputCategory').val().trim()
	if(brandField==""){
	$("#inputBrand").notify(
      "field can not be empty",
      { position:"top" }
    );
	    return
	}
	if(categoryField==""){
    	    $("#inputCategory").notify(
                  "field can not be empty",
                  { position:"top" }
                );
    	    return
    	}
    	var postingData = {
    	    brand:brandField,
    	    category:categoryField
    	}
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(postingData),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   		$.notify("ADDED SUCCESSFULLY",{ className:"success" })
	   		$form.find('#inputBrand').val('');
            $form.find('#inputCategory').val('');
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){

	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
    if(fileData.length>5000){
        $("#upload-brand-modal").notify("try a smaller file size",{className:"warn"})
        return;
    }
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	if(errorData.length==0)
    	$.notify("UPLOADED SUCCESSFULLY")
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
	        $.notify("ERROR IN UPLOADING",{className:"warn", globalPosition: 'top center' })
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	 var $table = $('#brand-table');

      // Destroy existing DataTable, if initialized
      if ($.fn.DataTable.isDataTable($table)) {
        $table.DataTable().destroy();
      }

      // Clear the table body
      $table.find('tbody').empty();

      // Populate the table with data
      for (var i in data) {
        var e = data[i];
        var buttonHtml = '<button class="btn btn-primary admin-element" onclick="displayEditBrand(' + e.id + ')"><i class="fa fa-pencil"></i></button>';
        var row = '<tr>'
          + '<td>' + e.id + '</td>'
          + '<td>' + e.brand.slice(0, 14) + '</td>'
          + '<td>' + e.category.slice(0, 14) + '</td>'
          + '<td>' + buttonHtml + '</td>'
          + '</tr>';
        $table.find('tbody').append(row);
      }

      // Initialize DataTable
      $table.DataTable({
        "paging": true, // Enable pagination
         "lengthMenu": [5, 10, 20], // Set the options for length change
         "pageLength": 5, // Set the initial page length
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

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}


function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
    //hide all elements of class admin-element when role = standard
    if($("#get-role").text().localeCompare("operator")==0) {
            $(".admin-element").hide();
        }
}

$(document).ready(init);
$(document).ready(getBrandList);
//$(document).ready(function() {
//	// Add button click event
//	$('#add-brand').click(addBrand);
//});
