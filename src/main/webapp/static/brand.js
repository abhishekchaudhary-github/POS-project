/**
remove delete function
**/

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var brandField = $("#brand-form").find('#inputBrand').val().trim()
	var categoryField = $("#brand-form").find('#inputCategory').val().trim()
	if(brandField==""){
	    $("#brand-form").find('#inputBrand').addClass('error');
	    return
	}
	if(categoryField==""){
    	    $("#brand-form").find('#inputCategory').addClass('error');
    	    return
    	}
    	var postingData = {
    	    brand:brandField,
    	    category:categoryField
    	}
//	$('#inputBrand').click(function() {
//    		$(this).removeClass('error');
//    	});
//    $('#inputCategory').click(function() {
//        		$(this).removeClass('error');
//        	});
//    console.log($('#inputBrand').val());
//    $('#inputBrand').brand.val('');
//    $('#inputBrand').category.val('');

//$("#brand-form").find('#inputBrand').value('')
//$("#brand-form").find('#inputCategory').val('')
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
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button onclick="deletebrand(' + e.id + ')">delete</button>'
		var buttonHtml = ''
		buttonHtml += ' <button onclick="displayEditBrand(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand+ '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
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
}

$(document).ready(init);
$(document).ready(getBrandList);
//$(document).ready(function() {
//	// Add button click event
//	$('#add-brand').click(addBrand);
//});
