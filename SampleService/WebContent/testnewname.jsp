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

						<button id="register" class="submitformtest">Register</button>
				</form>


			</div>
		</div>
	</div>
</body>
</html>