function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();
	var barcodeField = $form.find('#inputBarcode').val().trim();
    var brandField = $form.find('#inputBrand').val().trim()
    var categoryField = $form.find('#inputCategory').val().trim()
    var mrpField = $form.find('#inputMrp').val().trim()
    var nameField = $form.find('#inputName').val().trim()

//setting error class for error message and red field
    if(barcodeField==""){
    $("#inputBarcode").notify(
          "field can not be empty",
          { position:"top" }
        );
    	    return
    	}
    if(brandField==""){
    $("#inputBrand").notify(
              "field can not be empty",
              { position:"top" }
            );
    	    return
    	}
    if(mrpField==""){
    $("#inputMrp").notify(
              "field can not be empty",
              { position:"top" }
            );
    	    return
    	}
    if(nameField==""){
    $("#inputName").notify(
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
        barcode:barcodeField,
        brand:brandField,
        mrp:mrpField,
        name:nameField,
        category:categoryField
    }
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(postingData),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   		$.notify("ADDED SUCCESSFULLY",{ className:"success" , globalPosition: 'top center'})
	   		$form.find('#inputBarcode').val('');
	   		$form.find('#inputBrand').val('');
	   		$form.find('#inputCategory').val('');
	   		$form.find('#inputName').val('');
	   		$form.find('#inputMrp').val('');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('show');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	//var json = toJson($form);
    var barcodeField = $form.find('#inputBarcode1').val().trim();
        var brandField = $form.find('#inputBrand1').val().trim()
        var categoryField = $form.find('#inputCategory1').val().trim()
        var mrpField = $form.find('#inputMrp1').val().trim()
        var nameField = $form.find('#inputName1').val().trim()

	 if(barcodeField==""){
        $("#inputBarcode1").notify(
              "field can not be empty",
              { position:"top" }
            );
        	    return
        	}
        if(brandField==""){
        $("#inputBrand1").notify(
                  "field can not be empty",
                  { position:"top" }
                );
        	    return
        	}
        if(mrpField==""){
        $("#inputMrp1").notify(
                  "field can not be empty",
                  { position:"top" }
                );
        	    return
        	}
        if(nameField==""){
        $("#inputName1").notify(
                  "field can not be empty",
                  { position:"top" }
                );
        	    return
        	}
        if(categoryField==""){
        $("#inputCategory1").notify(
                      "field can not be empty",
                      { position:"top" }
                    );
            	    return
            	}
     var postingData = {
            barcode:barcodeField,
            brand:brandField,
            mrp:mrpField,
            name:nameField,
            category:categoryField
        }

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(postingData),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-product-modal').modal('hide');
	   		getProductList();
	   		$.notify("UPDATED SUCCESSFULLY",{ className:"success" , globalPosition: 'top center'})
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

//function deleteProduct(id){
//	var url = getProductUrl() + "/" + id;
//
//	$.ajax({
//	   url: url,
//	   type: 'DELETE',
//	   success: function(data) {
//	   		getProductList();
//	   },
//	   error: handleAjaxError
//	});
//}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	if(errorData.length==0){
        	$.notify("UPLOADED SUCCESSFULLY", {globalPosition: 'top center'})
    		return;
    	}
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getProductUrl();

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

function displayProductList(data){
	var $table =  $('#product-table');
	 // Destroy existing DataTable, if initialized
          if ($.fn.DataTable.isDataTable($table)) {
            $table.DataTable().destroy();
          }
	$table.find('tbody').empty();
	for(var i in data){
		var e = data[i];
//		var buttonHtml = '<button onclick="deleteProduct(' + e.id + ')">delete</button>'
		var buttonHtml = ' <button class="btn btn-primary admin-element" onclick="displayEditProduct(' + e.id + ')"><i class="fa fa-pencil"></i></button>'
		var row = '<tr>'
		+ '<td>'  + e.barcode.slice(0,10) + '</td>'
		+ '<td>'  + e.brand.slice(0,10) + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + e.name.slice(0,10) + '</td>'
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

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
     //hide all elements of class admin-element when role = standard
        if($("#get-role").text().localeCompare("operator")==0) {
                $(".admin-element").hide();
            }
}

$(document).ready(init);
$(document).ready(getProductList);

