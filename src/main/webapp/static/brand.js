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
	   		$('#addOrderDetail-modal').modal('hide');
	   		$.notify("ADDED SUCCESSFULLY",{ className:"success" , globalPosition: 'top center'})
	   		$form.find('#inputBrand').val('');
            $form.find('#inputCategory').val('');
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('show');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	var brandField = $form.find('#inputBrand1').val().trim()
	var categoryField = $form.find('#inputCategory1').val().trim()
	if(brandField==""){
    	$("#inputBrand1").notify(
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
        	    brand:brandField,
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
	        $('#edit-brand-modal').modal('hide');
	   		getBrandList();
	   		if(response == 1) {
	   		    $.notify("updated successfully",{ className:"success" , globalPosition: 'top center'})
	   		}
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
// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){

	var file = $('#brandFile')[0].files[0];
	console.log(file)
	if(file==null){
	    $.notify("select a valid tsv file",{globalPosition: 'top center', className:"warn"})
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	console.log(fileData)
    if(fileData.length>5000){
        $.notify("try a smaller file size",{globalPosition: 'top center',className:"warn"})
        return;
    }
    if(fileData.length==0){
        $.notify("no data in the file",{globalPosition: 'top center',className:"warn"})
        return;
    }
    if( !(fileData[0].hasOwnProperty('brand') || fileData[0].hasOwnProperty('category')) ) {
        $.notify("name of fields are not as per requirement",{globalPosition: 'top center',className:"error"})
        return;
    }
    console.log(fileData[0])
	for(var i =0 ;i < fileData.length; i++) {
	    console.log(fileData[i][0])
	    fileData[i].brand = fileData[i].brand.trim();
	    fileData[i].category = fileData[i].category.trim();
	}
	uploadRows();
}



function uploadRows(){
//// Disable elements with the "uploading-disabled" class
//  $('.uploading-disabled').prop('disabled', true);
    var row=[];
         while(processCount!=fileData.length){
            row.push(fileData[processCount]);
            processCount++;
         }
    var json = JSON.stringify(row);
   	var url = getBrandUrl()+'/tsv';
    //Make ajax call
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   if(data[data.length-1].errorCount==1)
    	    $.notify("error in uploading",{className:"warn", globalPosition: 'top center' })
    	   else
    	    $.notify("uploaded successfully",{className:"success", globalPosition: 'top center' })
    	    updateUploadDialog(data,data.length);
                           for(var i=0;i<data.length;i++){
                           let arr = {
                               brand : data[i].brand,
                               category : data[i].category,
                               error : data[i].message
                           }
                           console.log(data[i].message)
                           errorData.push(arr)
                	   }
    	   },
//    	    error: function() {
//    	        $.notify("format of the file is invalid",{className:"warn", globalPosition: 'top center' });
//    	    }
    	});
// $('.uploading-disabled').prop('disabled', false);
}

function downloadErrors(){
//if(errorData.length==0) {
//     $.notify("no errors",{className:"warn", globalPosition: 'top center' })
//     return;
//}
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
	updateUploadDialog2();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateUploadDialog2(){
	$('#rowCount').html("" + 0);
	$('#processCount').html("" + 0);
	$('#errorCount').html("" + 0);
}


    	    function updateUploadDialog(data,length){
    	        $('#rowCount').html("" + length);
    	        var count=0;
    	        for(var i =0;i<length;i++) {
    	            if(data[i].errorCount==1){
    	                count ++;
    	            }
    	        }
    	        if(count>0)
                $('#processCount').html("" + 0);
                else
                $('#processCount').html("" + length);
                $('#errorCount').html("" + count);
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

//// Function to show/hide the loader
//function toggleLoader(show) {
//  if (show) {
//    $("#loader").show();
//  } else {
//    $("#loader").hide();
//  }
//}

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
     $('#modal-add').click(function(){
        $('#inputBrand').val('');
        $('#inputCategory').val('');
     });
//      toggleLoader(true);
}

$(document).ready(init);
$(document).ready(getBrandList);
//$(document).ready(function() {
//	// Add button click event
//	$('#add-brand').click(addBrand);
//});
