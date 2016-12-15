<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="js/app-ajax.js"></script>
<link src="css/app.css"  rel="stylesheet"></link>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<title>List Of Users Registered</title>
</head>


<body>

<script type="text/javascript">

$( document ).ready(function() {
    $.ajax({
		type: "GET",
		data:{"getAllValues" : "true"},
		url: "UserRegistrationServlet", 
		success: function(responseJson){
			var $table = $("<table border='2' class='table'>").appendTo($("#result")); 
			$("<tr>").appendTo($table)
			         .append($("<td>").text("FULL NAME"))
			         .append($("<td>").text("AGE"))
					 .append($("<td>").text("QUALIFICATION"))
					 .append($("<td>").text("YEAR PASSED"))
			.append($("<td>").text("GENDER"))
			.append($("<td>").text("INTERESTS"))
			.append($("<td>").text("ACTIONS"));
			$("</tr>").appendTo($table);
			$.each(responseJson, function(index, user) {   
	            $("<tr>").appendTo($table)
	            		.append($("<td>").text(user.fullName))
	                	.append($("<td>").text(user.age))
	            		.append($("<td>").text(user.qualification))
	            		.append($("<td>").text(user.yearPassed))
	            		.append($("<td>").text(user.gender))
	            		.append($("<td>").text(user.interests))
	            		.append($("<td>").html('<button id="editUser" name='+user.id+' class="action" >Edit</button>'));
	            $("</tr>").appendTo($table);
	        });
		},
		error: function(){
			$('#result').html("Exception!!");
		}
	});
    
    
});
</script>

	<script type="text/javascript">
		$(document).on('click', '.action', function() {
			var id = $(this).attr('id');
			var name = $(this).attr('name');
			if (id = 'editUser') {
				window.location.href = '/SampleWebApp?name='+name; 
			}
		});
	</script>


	<div class="panel panel-default">
		<div class="panel-heading">
			<span class="lead">List of Users </span>
		</div>
		<div class="tablecontainer">
		<table class="table" id="result">
		</table>
		
		</div>
	</div>
</body>
</html>