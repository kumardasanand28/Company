<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="js/app-ajax.js"></script>
<link src="css/app.css"  rel="stylesheet"></link>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Data</title>
</head>
<style>
div.ex {
	text-align: right width:300px;
	padding: 10px;
	border: 5px solid grey;
	margin: 0px
}
</style>

<button id="verifyConnection" class="verifyConnection">Verify Connection</button>
<script type="text/javascript">

$(document).on('click', '.verifyConnection', function(event){
	alert('Going to verify database connection');
	$.ajax({
		type: "POST",
		url: "UserRegistrationServlet"+"?action=verifyConnection", 
		success: function(msg){
			$('#result').html(msg);
		},
		error: function(){
			$('#result').html(msg);
		}
	});
	 event.preventDefault();
});
</script>

<button id="createtable" class="createtable">Create Table in MySQL</button>
<script type="text/javascript">

$(document).on('click', '.createtable', function(event){
	alert('Going to create database');
	$.ajax({
		type: "POST",
		url: "UserRegistrationServlet"+"?action=createDatabase", 
		success: function(msg){
			console.log(msg);
		},
		error: function(){
			console.log("Error");
		}
	});
	 event.preventDefault();
});
</script>
	
	
<script type="text/javascript">


$(document).ready(function(){
	
	var param = "<%=request.getParameter("name")%>";
		if (param != 'null') {
			$.ajax({
				type : "GET",
				data : {
					"getAllValues" : "false",
					"name" : param
				},
				url : "UserRegistrationServlet",
				success : function(responseJson) {
					$('#name').val(responseJson.fullName);
					$('#age').val(responseJson.age);
					$('#qual').val(responseJson.qualification);
					$('#yop').val(responseJson.yearPassed);
					if (responseJson.gender == 'male') {
						$("#male").prop("checked", true);
					} else {
						$("#female").prop("checked", true);
					}
					var interests = responseJson.interests;
					var arrayLength = interests.length;
					for (var i = 0; i < arrayLength; i++) {
						 if(interests[i].trim() == 'cricket'){
			            	$("#cricket").prop("checked", true); 
		               	} 
						if(interests[i].trim() == 'hockey'){
			            	$("#hockey").prop("checked", true); 
		               	}
					 	else if(interests[i].trim() == 'football'){
			            	$("#football").prop("checked", true); 
		               	}
						else if(interests[i].trim() == 'tt'){
			            	$("#tt").prop("checked", true); 
		               	} 
					}
				},
				error : function() {
					$('#result').html("Exception!!");
				}
			});
		}
	});
</script>


<body class="ng-cloak">

	<div id="result"></div>

	<div class="generic-container">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span class="lead">User Registration Form </span>
			</div>
			<div class="formcontainer">


				<form id="register">

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Full
								Name</label>
							<div class="col-md-7">
								<input type="text" name="fullname" id="name"
									class="username form-control input-sm" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Age</label>
							<div class="col-md-7">
								<input type="text" name="age" id="age"
									class="form-control input-sm" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Qualification</label>
							<div class="col-md-7">
								<input type="text" name="qual" id="qual"
									class="form-control input-sm" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Year
								Passed</label>
							<div class="col-md-7">
								<input type="text" name="yop" id="yop"
									class="form-control input-sm" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Gender</label>
							<div class="col-md-7">
								<input type="radio" name="gender" value="male" id="male" /> Male
								<input type="radio" name="gender" value="female" id="female" />Female
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="file">Interests</label>
							<div class="col-md-7">
								<input type="checkbox" name="interest" value="cricket" id="cricket"/>Cricket
								<input type="checkbox" name="interest" value="hockey" id="hockey"/>Hockey
								<input type="checkbox" name="interest" value="football" id="football"/>FootBall
								<input type="checkbox" name="interest" value="tt" id="tt"/>Table
							</div>
						</div>
					</div>


					<c:choose>
						<c:when test="${!empty param.name}">
							<button name="${param.name}" id="update" class="submitform">Update</button>
						</c:when>
						<c:otherwise>
							<button id="register" class="submitform">Register</button>
						</c:otherwise>
					</c:choose>

				</form>


			</div>
		</div>
	</div>
	<a href="/listUsers.jsp">List Users</a>
</body>
</html>