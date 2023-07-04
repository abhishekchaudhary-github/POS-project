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
    	    $form.find('#inputBarcode').addClass('error');
        	document.getElementById("errorMessageBarcode").style.visibility = "visible";
    	    return
    	}
    if(brandField==""){
    	    $form.find('#inputBrand').addClass('error');
        	document.getElementById("errorMessageBrand").style.visibility = "visible";
    	    return
    	}
    if(mrpField==""){
    	    $form.find('#inputMrp').addClass('error');
        	document.getElementById("errorMessageMrp").style.visibility = "visible";
    	    return
    	}
    if(nameField==""){
    	    $form.find('#inputName').addClass('error');
        	document.getElementById("errorMessageName").style.visibility = "visible";
    	    return
    	}
    if(categoryField==""){
        	    $form.find('#inputCategory').addClass('error');
            	document.getElementById("errorMessageCategory").style.visibility = "visible";
        	    return
        	}

        	$form.find('#inputBarcode').click(function() {
                		$(this).removeClass('error');
                	    document.getElementById("errorMessageBarcode").style.visibility = "hidden";
                	});
            $form.find('#inputBrand').click(function() {
                		$(this).removeClass('error');
                	    document.getElementById("errorMessageBrand").style.visibility = "hidden";
                	});
            $form.find('#inputMrp').click(function() {
                		$(this).removeClass('error');
                	    document.getElementById("errorMessageMrp").style.visibility = "hidden";
                	});
            $form.find('#inputName').click(function() {
                		$(this).removeClass('error');
                	    document.getElementById("errorMessageName").style.visibility = "hidden";
                	});
            $form.find('#inputCategory').click(function() {
                		$(this).removeClass('error');
                	    document.getElementById("errorMessageCategory").style.visibility = "hidden";
                	});

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
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
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
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
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
//		var buttonHtml = '<button onclick="deleteProduct(' + e.id + ')">delete</button>'
		var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')" class="admin-element">edit</button>'
		var row = '<tr>'
		+ '<td>'  + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if($("#get-role").text().localeCompare("standard")==0) {
                    $(".admin-element").hide();
                }
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
        if($("#get-role").text().localeCompare("standard")==0) {
                $(".admin-element").hide();
            }
}

$(document).ready(init);
$(document).ready(getProductList);

