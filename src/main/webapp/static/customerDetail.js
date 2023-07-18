var addedData = [];
var jsonData = [];
var checker = true
var prodId = -1;
var editOrderData;
function getCustomerDetailUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem";
}

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/invoice";
}


function checkProduct(data, fileData) {
var k =0;
    for( i in data ) {
        var e = data[i];
        if(data[i].barcode.localeCompare(fileData[0].value.trim())==0){
        if(data[i].mrp < fileData[1].value){
        $("#customerDetail-form").notify("Selling price cannot be greater than MRP",{className:"warn"})
            checker = false
            }
            prodId = data[i].id;
            k=1;
            break;
        }
        else checker = true
    }
   if(k==0){
   $("#customerDetail-form").notify("barcode does not exist",{className:"warn"})
    checker = false}
}


function checkInventory(data, fileData){
    if(data.quantity - fileData<0){
    $("#customerDetail-form").notify("value accessed the inventor",{className:"warn"})
        checker = false
    }
    else check = true;
}







//BUTTON ACTIONS
function addCustomerDetail(event){
	var $form = $("#customerDetail-form");
	var $confirm = $('#confirm');
	$confirm.empty();
	var fileData = $form.serializeArray();
	var $tbody = $('#customerDetail-table').find('tbody');
	$tbody.empty();
	fileData[0].value = fileData[0].value.toLowerCase();
	//checks

checker = true
var url = $("meta[name=baseUrl]").attr("content") + "/api/product";

	$.ajax({
	   url: url,
	   type: 'GET',
	   async: false,
	   success: function(data) {
	   		checkProduct(data,fileData);
	   },
	   error: handleAjaxError
	});



if(checker){
var iurl = $("meta[name=baseUrl]").attr("content") + "/api/inventory" + "/" + prodId;
	$.ajax({
	   url: iurl,
	   type: 'GET',
	   async: false,
	   success: function(data) {
	   console.log("hi")
	   		checkInventory(data,fileData[2].value);
	   },
	   error: handleAjaxError
	});
}
//console.log("hi2")
	var error = false;
if(!checker) error = true






	if(fileData[0].value.trim().localeCompare("")==0||fileData[1].value.localeCompare("")==0||fileData[2].value.localeCompare("")==0) {
	    $("#customerDetail-form").notify("fields can not be empty",{className:"warn"})
	    error = true;
	}



    else if(isNaN(fileData[1].value)){
        $("#customerDetail-form").notify("MRP can not be in this format",{className:"warn"})
        error=true;
    }
    else if(parseFloat(fileData[1].value)<0){
    $("#customerDetail-form").notify("MRP can't be negative",{className:"warn"})
            error=true;
    }

       else if(isNaN(fileData[2].value)){
            $("#customerDetail-form").notify("Quantity must be an integer",{className:"warn"})
            error = true;
        }
        else if(!Number.isInteger(parseFloat(fileData[2].value))){
            $("#customerDetail-form").notify("Quantity must be a whole number",{className:"warn"})
            error = true;
        }
        else if(parseInt(fileData[2].value)<0){
                $("#customerDetail-form").notify("Quantity must be positive",{className:"warn"})
                error = true;
            }

	for(var i in addedData){
	    var e = addedData[i];
	    if(e.barcode.localeCompare(fileData[0].value.trim())==0){
	        $("#customerDetail-form").notify("same barcode has been entered already",{className:"warn"})
	        error = true;
	        break;
	    }
	}
	if(error==false){
        addedData.push({barcode: fileData[0].value.trim() ,mrp: parseFloat(fileData[1].value),quantity: fileData[2].value })
        var jsonObject ={barcode: fileData[0].value.trim(), mrp: parseFloat(fileData[1].value),quantity: parseInt(fileData[2].value)}
        //jsonObject = JSON.stringify(jsonObject,["barcode","mrp","quantity"])

        jsonData.push(
                jsonObject
        )
	}
	for(var i in addedData){
    		var e = addedData[i];
    		buttonHtml = ' <button onclick="displayEditCustomerDetail(' + i + ')" class="btn btn-primary"><i class="fa fa-pencil"></i></button>'
    		buttonHtml = buttonHtml + ' <button onclick="deleteEditCustomerDetail(' + i + ')" class="btn btn-danger"><i class="fa fa-trash"></i></button>'
    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>' //barcode
    		+ '<td>'  + e.mrp + '</td>' //mrp
    		+ '<td>'  + e.quantity + '</td>' //quantity
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row)
    	}
    	var confirmButton;
    	$confirm.empty();
    if(addedData.length>0){
            confirmButton = '<td>' + ' <button onclick="submitCustomerDetail(' + e.id + ')" class="btn btn-success"><i class="fa fa-check"></i> Confirm</button>' + '</td>'
    	    $confirm.append(confirmButton)
    	}
    	$('#inputBarcode').val('');
        $('#inputMrp').val('');
        $('#inputQuantity').val('');
}



function deleteEditCustomerDetail(id) {
    addedData.splice(id, 1)
    DisplayAddedData()
}

function DisplayAddedData(){
    	var $tbody = $('#customerDetail-table').find('tbody');
    	var $confirm = $('#confirm');
    	$confirm.empty();
    	$tbody.empty()
    	for(var i in addedData){
            		var e = addedData[i];
            		buttonHtml = ' <button onclick="displayEditCustomerDetail(' + i + ')" class="btn btn-primary"><i class="fa fa-pencil"></i></button>'
                        		buttonHtml = buttonHtml + ' <button onclick="deleteEditCustomerDetail(' + i + ')" class="btn btn-danger"><i class="fa fa-trash"></i></button>'
            		var row = '<tr>'
            		+ '<td>' + e.barcode + '</td>' //barcode
            		+ '<td>'  + e.mrp + '</td>' //mrp
            		+ '<td>'  + e.quantity + '</td>' //quantity
            		+ '<td>' + buttonHtml + '</td>'
            		+ '</tr>';
                    $tbody.append(row)
            	}
            	var confirmButton;
            	$confirm.empty();
            if(addedData.length>0){
                $confirm.empty();
                confirmButton =  '<button onclick="submitCustomerDetail(' + e.id + ')" class="btn btn-success"><i class="fa fa-check"></i> Confirm</button>'
            	    $confirm.append(confirmButton)
            	}
            	$('#inputBarcode').val('');
                $('#inputMrp').val('');
                $('#inputQuantity').val('');
        	return false;
}






function submitCustomerDetail(){
//FUNCTION IS HERE
    var stringifyJsonData =  JSON.stringify(jsonData)

	var url = getCustomerDetailUrl();
	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: JSON.stringify(jsonData),
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function() {

    	       $.notify("ADDED SUCCESSFULLY", { className: "success", globalPosition: 'top center' });
                    setTimeout(function () {
                        location.reload();
                    }, 1000);
    	   },
    	   error: handleAjaxError,
    	});
//refresh the page
}

function updateCustomerDetail(event){
	$('#edit-customerDetail-modal').modal('toggle');
	//Get the ID
//	var id = $("#customerDetail-edit-form input[name=id]").val();
//	var url = getCustomerDetailUrl() + "/" + id;
//	//Set the values to update
	var $form = $("#customerDetail-edit-form");
	var json = toJson($form);
	var fileData = $form.serializeArray();

	var $tbody = $('#customerDetail-table').find('tbody');
	var barcodeChange = false;











checker = true
var url = $("meta[name=baseUrl]").attr("content") + "/api/product";

	$.ajax({
	   url: url,
	   type: 'GET',
	   async: false,
	   success: function(data) {
	   		checkProduct(data,fileData);
	   },
	   error: handleAjaxError
	});



if(checker){
var iurl = $("meta[name=baseUrl]").attr("content") + "/api/inventory" + "/" + prodId;
	$.ajax({
	   url: iurl,
	   type: 'GET',
	   async: false,
	   success: function(data) {
	   console.log("hi")
	   		checkInventory(data,fileData[2].value);
	   },
	   error: handleAjaxError
	});
}
//console.log("hi2")
	var error = false;
if(!checker) error = true















	if(fileData[0].value.trim().localeCompare(addedData[fileData[3].value.trim()].barcode)!=0){
	//console.log(fileData[0].value)
	//console.log(addedData[fileData[3].value].barcode)
	    barcodeChange=true;
	}

    $tbody.empty();
	fileData[0].value = fileData[0].value.trim().toLowerCase();
	//checks
	if(fileData[0].value.trim().localeCompare("")==0||fileData[1].value.localeCompare("")==0||fileData[2].value.localeCompare("")==0) {
	    $("#customerDetail-form").notify("fields can not be empty",{className:"warn"})
	    error = true;
	}

    else if(isNaN(fileData[1].value)){
        $("#customerDetail-form").notify("Quantity must be an integer",{className:"warn"})
        error = true;
    }

    else if(isNaN(fileData[2].value)){
        $("#customerDetail-form").notify("MRP can not be in this format",{className:"warn"})
        error=true;
    }

else if(barcodeChange==true){
        for(var i in addedData){
            var e = addedData[i];
            if(e.barcode.localeCompare(fileData[0].value.trim())==0){
                $("#customerDetail-form").notify("same barcode has been entered already",{className:"warn"})
                error = true;
                break;
            }
        }
	}

	if(error==false){
        var iterationNumber = 0 ;
       	var tempData = addedData.map(item => {
           if(iterationNumber ++ == fileData[3].value) {
           return { barcode: fileData[0].value.trim(),mrp: parseFloat(fileData[1]).value,quantity: fileData[2].value }
            }
            else
            return item
           })
           addedData = tempData
        iterationNumber = 0 ;
        var tempData1 = jsonData.map(item => {
                   if(iterationNumber ++ == fileData[3].value) {
                   return { barcode: fileData[0].value.trim(),mrp: parseFloat(fileData[1].value),quantity: parseInt(fileData[2].value) }
                    }
                    else
                    return item
                   })
                   jsonData = tempData1

	}
	for(var i in addedData){
    		var e = addedData[i];
    		buttonHtml = ' <button onclick="displayEditCustomerDetail(' + i + ')" class="btn btn-primary"><i class="fa fa-pencil"></i></button>'
            buttonHtml = buttonHtml + ' <button onclick="deleteEditCustomerDetail(' + i + ')" class="btn btn-danger"><i class="fa fa-trash"></i></button>'
    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>' //barcode
    		+ '<td>'  + e.mrp + '</td>' //mrp
    		+ '<td>'  + e.quantity + '</td>' //quantity
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row)
    	}
    	var confirmButton;
    var $confirm = $('#confirm');
    $confirm.empty();
    if(addedData.length>0){
            confirmButton = '<td>' + ' <button onclick="submitCustomerDetail(' + e.id + ')" class="btn btn-success"><i class="fa fa-check"></i> Confirm</button>' + '</td>'
    	    $confirm.append(confirmButton)
    	}
	return false;
}


function getCustomerDetailList(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")+"/api/order"
	$.ajax({
	   url: baseUrl,
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
  var $table = $('#orderid-list-table');

  // Destroy existing DataTable, if initialized
  if ($.fn.DataTable.isDataTable($table)) {
    $table.DataTable().destroy();
  }

  // Clear the table body
  $table.find('tbody').empty();

  // Populate the table with data
  for (var i in data) {
    var e = data[i];
    var buttonHtml = '<button class="btn btn-info" onclick="orderDetail(' + e.id + ')"><i class="fa fa-eye"></i> Details</button>'
      + ' <button class="btn btn-danger admin-element" onclick="generateInvoice(' + e.id + ')"><i class="fa fa-file-pdf-o"></i> Invoice</button>'
      + '<button type="button" class="btn btn-secondary" onclick="deleteTheOrder(' + e.id + ')">Delete</button>'
    var row = '<tr>'
      + '<td>' + e.id + '</td>'
      + '<td>' + e.time + '</td>'
      + '<td>' + buttonHtml + '</td>'
      + '</tr>';
    $table.find('tbody').append(row);
  }

  // Initialize DataTable
  $table.DataTable({
    "paging": true, // Enable pagination
    "lengthChange": true, // Enable length change
    "lengthMenu": [5, 10, 20], // Set the options for length change
    "pageLength": 5, // Set the initial page length
    "searching": true, // Disable search functionality
    "ordering": false, // Disable sorting
    "info": false, // Hide information display
    "autoWidth": false, // Disable auto width calculation
    "responsive": true, // Enable responsive mode
    "columnDefs": [
      { "targets": "_all", "className": "text-center" } // Center align all columns
    ]
  });

  if ($("#get-role").text().localeCompare("operator") == 0) {
    $(".admin-element").hide();
  }
}

function generateInvoice(id) {
      var invoiceUrl = getInvoiceUrl() + '/' + id;
      window.open(invoiceUrl, '_blank');
}

function orderDetail(id) {
    var url = getCustomerDetailUrl();
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	        var $tbody = $('#order-detail-modal-body').find('tbody')
    	        $tbody.empty()
                for(var i in data) {
                        var e = data[i]
                        if(e.orderId==id) {
                            var cost = e.sellingPrice * e.quantity;
                            var row =
                            '<tr>'+
                            '<td>'  + e.name + '</td>' +
                            '<td>'  + e.quantity + '</td>' +
                            '<td>'  + e.sellingPrice + '</td>' +
                            '<td>'  + cost + '</td>' +
                            '<td>'  + ' <button onclick="displayEditOrderDetail(' + e.id + ')" class="btn btn-primary"><i class="fa fa-pencil"></i></button>'
                            + ' <button onclick="deleteEditOrderDetail(' + e.id + ')" class="btn btn-danger"><i class="fa fa-trash"></i></button>'
                             + '</td>' +
                            '</tr>';
                            $tbody.append(row);
                        }
                }
                $('#order-detail-modal').modal('toggle');
    	   },
    	   error: handleAjaxError
    	});
}

function displayEditOrderDetail(id){
    var url = getCustomerDetailUrl() + "/" + id;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   console.log(data)
    	   		displayOrderItemDetail(data);
    	   },
    	   error: handleAjaxError
    	});
}

function displayOrderItemDetail(data){
	    $("#edit-order-form input[name=quantity]").val(data.quantity);
    	$("#edit-order-form input[name=sellingPrice]").val(data.sellingPrice);
    	editOrderData = data;
    	console.log(editOrderData)
//    	 //when modal opens
//                        $('#edit-order-modal').on('shown.bs.modal', function (e) {
//                          $("#pageContent").css({ opacity: 0.9 });
//                        })
    	$('#edit-order-modal').modal('toggle');

}

function deleteEditOrderDetail(id) {
     var url = getCustomerDetailUrl() + "/" + id;
        	$.ajax({
        	   url: url,
        	   type: 'DELETE',
        	   success: function(data) {
        	   		location.reload();
        	   },
        	   error: handleAjaxError
        	});
}

function displayEditCustomerDetail(id){
	//var url = getCustomerDetailUrl() + "/" + id;
	 var data = addedData[id]
	 $("#customerDetail-edit-form input[name=barcode]").val(data.barcode);
     $("#customerDetail-edit-form input[name=mrp]").val(data.mrp);
     $("#customerDetail-edit-form input[name=quantity]").val(data.quantity);
     $("#customerDetail-edit-form input[name=id]").val(id);
     $('#edit-customerDetail-modal').modal('toggle');
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

function displayCustomerDetail(id){
	$("#customerDetail-edit-form input[name=barcode]").val(data.barcode);
	$("#customerDetail-edit-form input[name=mrp]").val(data.mrp);
	$("#customerDetail-edit-form input[name=quantity]").val(data.quantity);
	$("#customerDetail-edit-form input[name=id]").val(data.id);
	$('#edit-customerDetail-modal').modal('toggle');
}

function modalCLose() {
        $("#edit-customerDetail-modal").modal('hide')
}
function modalClose2() {
        $("#edit-order-modal").modal('hide')
}


function deleteTheOrder(id) {
    var url = getOrderUrl() + '/' + id
    $.ajax({
    	   url: url,
    	   type: 'DELETE',
    	   success: function(data) {
    	   		location.reload();
    	   },
    	   error: handleAjaxError
    	});
}

function updateEditedOrder() {
    $('#edit-order-modal').modal('toggle');
    var $form = $('#edit-order-form');
    var json = toJson($form);
    var fileData = $form.serializeArray();
    var objectForm = {
        id : editOrderData.id,
        orderId : editOrderData.orderId,
        productId : editOrderData.productId,
        quantity : parseInt(fileData[0].value),
        selling_price : parseFloat(fileData[1].value)
    }
    var url = getCustomerDetailUrl() + '/' + editOrderData.id;

    $.ajax({
        	   url: url,
        	   type: 'PUT',
        	   data: JSON.stringify(objectForm),
        	   headers: {
               	'Content-Type': 'application/json'
               },
        	   success: function() {

        	       $.notify("ADDED SUCCESSFULLY", { className: "success", globalPosition: 'top center' });
                        setTimeout(function () {
                            location.reload();
                        }, 1000);
        	   },
        	   error: handleAjaxError,
        	});

}

//INITIALIZATION CODE
function init(){
	$('#add-customerDetail').click(addCustomerDetail);
	$('#update-customerDetail').click(updateCustomerDetail);
	$('#update-edited-order').click(updateEditedOrder);
	$('#refresh-data').click(getCustomerDetailList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#customerDetailFile').on('change', updateFileName)
    	if($("#get-role").text().localeCompare("operator")==0) {
                        $(".admin-element").hide();
                    }
    $('.edit-cancel').click(modalCLose)
    $('.edit-order-cancel').click(modalClose2)
}
$.notify.defaults({globalPosition: 'top left'})
$(document).ready(init);
$(document).ready(getCustomerDetailList);