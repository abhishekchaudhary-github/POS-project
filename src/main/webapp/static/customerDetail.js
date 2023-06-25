var addedData = [];
function getCustomerDetailUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/customerDetail";
}

//BUTTON ACTIONS
function addCustomerDetail(event){
	var $form = $("#customerDetail-form");
	var fileData = $form.serializeArray();
	var $tbody = $('#customerDetail-table').find('tbody');
	$tbody.empty();
	fileData[0].value = fileData[0].value.toLowerCase();
	//checks
	var error = false;
	if(fileData[0].value.localeCompare("")==0||fileData[1].value.localeCompare("")==0||fileData[2].value.localeCompare("")==0) {
	    alert("fields can not be empty")
	    error = true;
	}

    if(isNaN(fileData[1].value)){
        alert("Quantity must be an integer")
        error = true;
    }

    if(isNaN(fileData[2].value)){
        alert("MRP can not be in this format")
        error=true;
    }

	for(var i in addedData){
	    var e = addedData[i];
	    if(e.barcode.localeCompare(fileData[0].value)==0){
	        alert("same barcode has been entered already")
	        error = true;
	        break;
	    }
	}
	if(error==false)
	addedData.push({barcode: fileData[0].value,mrp: fileData[1].value,quantity: fileData[2].value})
	for(var i in addedData){
    		var e = addedData[i];
    		console.log(e);
    		buttonHtml = ' <button onclick="displayEditCustomerDetail(' + e.id + ')">edit</button>'
    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>' //barcode
    		+ '<td>'  + e.mrp + '</td>' //mrp
    		+ '<td>'  + e.quantity + '</td>' //quantity
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
}

function updateCustomerDetail(event){
	$('#edit-customerDetail-modal').modal('toggle');
	//Get the ID
	var id = $("#customerDetail-edit-form input[name=id]").val();
	var url = getCustomerDetailUrl() + "/" + id;

	//Set the values to update
	var $form = $("#customerDetail-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getCustomerDetailList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getCustomerDetailList(){
	var url = getCustomerDetailUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCustomerDetailList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteCustomerDetail(id){
	var url = getCustomerDetailUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getCustomerDetailList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){

	var file = $('#customerDetailFile')[0].files[0];
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
    var id = row.id;
	var json = JSON.stringify(row);
	var url = getCustomerDetailUrl() + "/" + id;

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

function displayCustomerDetailList(data){
	var $tbody = $('#customerDetail-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button onclick="deletecustomerDetail(' + e.id + ')">delete</button>'
		var buttonHtml = ''
		buttonHtml += ' <button onclick="displayEditCustomerDetail(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditCustomerDetail(id){
	var url = getCustomerDetailUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCustomerDetail(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#customerDetailFile');
	$file.val('');
	$('#customerDetailFileName').html("Choose File");
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
	var $file = $('#customerDetailFile');
	var fileName = $file.val();
	$('#customerDetailFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-customerDetail-modal').modal('toggle');
}

function displayCustomerDetail(data){
	$("#customerDetail-edit-form input[name=barcode]").val(data.barcode);
	$("#customerDetail-edit-form input[name=quantity]").val(data.quantity);
	$("#customerDetail-edit-form input[name=mrp]").val(data.mrp);
	$("#customerDetail-edit-form input[name=id]").val(data.id);
	$('#edit-customerDetail-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-customerDetail').click(addCustomerDetail);
	$('#update-customerDetail').click(updateCustomerDetail);
	$('#refresh-data').click(getCustomerDetailList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#customerDetailFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getCustomerDetailList);
