/**
remove delete function
**/

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var barcodeField = $form.find('#inputBarcode').val().trim()
    var quantityField = $form.find('#inputQuantity').val().trim()
	var json = toJson($form);
	var url = getInventoryUrl();
	if(barcodeField==""){
    	$("#inputBarcode").notify(
          "field can not be empty",
          { position:"top" }
        );
    	    return
    	}
    if(quantityField==""){
    	$("#inputQuantity").notify(
          "field can not be empty",
          { position:"top" }
        );
    	    return
    	}

    var postingData = {
    	    barcode:barcodeField,
    	    quantity:quantityField
    	}

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(postingData),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
	   		$.notify("UPDATED SUCCESSFULLY",{ className:"success" , globalPosition: 'top center'})
	   		$form.find('#inputBarcode').val('');
            $form.find('#inputQuantity').val('');
	   },
	   error: handleAjaxError
	});



	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
//	var arr;
//	var url = getInventoryUrl();
//    	$.ajax({
//    	   url: url,
//    	   type: 'GET',
//    	   async: false,
//    	   success: function(data) {
//    	   		    arr.push(data);
//    	   		    console.log(arr);
//    	   },
//    	   error: handleAjaxError
//    	});
//
	url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var barcodeField = $form.find('#inputBarcode1').val().trim()
        var quantityField = $form.find('#inputQuantity1').val().trim()
    	var json = toJson($form);
    	if(barcodeField==""){
        	$("#inputBarcode1").notify(
              "field can not be empty",
              { position:"top" }
            );
        	    return
        	}
        if(quantityField==""){
        	$("#inputQuantity1").notify(
              "field can not be empty",
              { position:"top" }
            );
        	    return
        	}

        var postingData = {
        	    barcode:barcodeField,
        	    quantity:quantityField
        	}

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(postingData),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-inventory-modal').modal('hide');
	   		getInventoryList();
	   		$.notify("UPDATED SUCCESSFULLY",{ className:"success" , globalPosition: 'top center'})
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){

	var file = $('#inventoryFile')[0].files[0];
	if(file==null){
    	    $.notify("select a valid tsv file",{globalPosition: 'top center', className:"warn"})
    	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	 if(fileData.length>5000){
            $("#upload-brand-modal").notify("try a smaller file size",{className:"warn"})
            return;
        }
	for(var i =0 ;i < fileData.length; i++) {
	    console.log(fileData[i][0])
	    fileData[i].barcode = fileData[i].barcode.trim();
	    fileData[i].quantity = fileData[i].quantity.trim();
	    fileData[i].quantity = parseInt(fileData[i].quantity)
	}
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	if(errorData.length==0){
        	$.notify("uploaded successfully", {globalPosition: 'top center',className:"success"})
            $('#upload-brand-modal').modal('hide');
        }
        else
    	    $.notify("error in uploading",{className:"warn", globalPosition: 'top center' })
    	return;
    	}

	//Process next row
	var row = fileData[processCount];
	processCount++;
    var barcode = row.barcode;
	var json = JSON.stringify(row);

	var url = getInventoryUrl() + "/" + id;

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'PUT',
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
if(errorData.length==0) {
     $.notify("no errors",{className:"warn", globalPosition: 'top center' })
     return;
}
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $table = $('#inventory-table');

          // Destroy existing DataTable, if initialized
          if ($.fn.DataTable.isDataTable($table)) {
            $table.DataTable().destroy();
          }

          // Clear the table body
          $table.find('tbody').empty();

	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button onclick="deleteinventory(' + e.id + ')">delete</button>'
		var buttonHtml = ''
		buttonHtml += ' <button class="btn btn-primary admin-element" onclick="displayEditInventory(' + e.id + ')"><i class="fa fa-pencil"></i></button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
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

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
        //hide all elements of class admin-element when role = standard
        if($("#get-role").text().localeCompare("operator")==0) {
                $(".admin-element").hide();
            }
     $('#modal-add').click(function(){
            $('#inputBarcode').val('');
            $('#inputQuantity').val('');
         });
}

$(document).ready(init);
$(document).ready(getInventoryList);