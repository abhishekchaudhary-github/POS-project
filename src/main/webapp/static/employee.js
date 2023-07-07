function getEmployeeUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/employee";
}

//BUTTON ACTIONS
function addEmployee(event){
	//Set the values to update
	var $form = $("#employee-form");
	var json = toJson($form);
	var url = getEmployeeUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getEmployeeList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateEmployee(event){
	$('#edit-employee-modal').modal('toggle');
	//Get the ID
	var id = $("#employee-edit-form input[name=id]").val();
	var url = getEmployeeUrl() + "/" + id;

	//Set the values to update
	var $form = $("#employee-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getEmployeeList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getEmployeeList(){
	var url = getEmployeeUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayEmployeeList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteEmployee(id){
	var url = getEmployeeUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getEmployeeList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#employeeFile')[0].files[0];
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
	var url = getEmployeeUrl();

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

function displayEmployeeList(data){
	 var $table = $('#employee-table');

      // Destroy existing DataTable, if initialized
      if ($.fn.DataTable.isDataTable($table)) {
        $table.DataTable().destroy();
      }

      // Clear the table body
      $table.find('tbody').empty();

      // Populate the table with data
      for (var i in data) {
        var e = data[i];
        var buttonHtml = '<button onclick="deleteEmployee(' + e.id + ')">delete</button>';
        buttonHtml += ' <button class="btn btn-primary" onclick="displayEditEmployee(' + e.id + ')"><i class="fa fa-pencil"></i> </button>';
        var row = '<tr>'
          + '<td>' + e.id + '</td>'
          + '<td>' + e.name + '</td>'
          + '<td>' + e.age + '</td>'
          + '<td>' + buttonHtml + '</td>'
          + '</tr>';
        $table.find('tbody').append(row);
      }

      // Initialize DataTable with sorting disabled
        $table.DataTable({
          "ordering": false
        });
// Apply styles to table header cells
  $table.find('th').css('text-align', 'center');
}

function displayEditEmployee(id){
console.log(id)
	var url = getEmployeeUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayEmployee(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#employeeFile');
	$file.val('');
	$('#employeeFileName').html("Choose File");
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
	var $file = $('#employeeFile');
	var fileName = $file.val();
	$('#employeeFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-employee-modal').modal('toggle');
}

function displayEmployee(data){
console.log(data)
	$("#employee-edit-form input[name=name]").val(data.name);
	$("#employee-edit-form input[name=age]").val(data.age);
	$("#employee-edit-form input[name=id]").val(data.id);
	$('#edit-employee-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-employee').click(addEmployee);
	$('#update-employee').click(updateEmployee);
	$('#refresh-data').click(getEmployeeList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#employeeFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getEmployeeList);
$(document).ready(function() {
      $("#validateButton").click(function() {
        var name = $("#inputName").val();
        var age = $("#inputAge").val();

        // Check if the name field is empty
        if (name === "") {
          $("#nameError").removeClass("d-none");
          showDialog("Please enter a name.");
          return;
        } else {
          $("#nameError").addClass("d-none");
        }

        // Check if the age field is empty
        if (age === "") {
          $("#ageError").removeClass("d-none");
          showDialog("Please enter an age.");
          return;
        } else {
          $("#ageError").addClass("d-none");
        }

        // Validation passed, perform further actions
        // ...
      });

      function showDialog(message) {
        BootstrapDialog.show({
          title: 'Validation Error',
          message: message,
          buttons: [{
            label: 'OK',
            action: function(dialog) {
              dialog.close();
            }
          }]
        });
      }
    });
