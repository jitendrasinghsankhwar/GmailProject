<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Page</title>
<link href="resources/mytheme/css/style.css" rel="stylesheet">

<script src="resources/mytheme/js/jquery.js"></script>
<script src="resources/mytheme/js/jquery.validate.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<script>
		
	</script>
<style type="text/css">
.error {
	color: red;
	margin-left: 130px;
}
</style>
</head>

<body style="background: #E0E0D1;">
	<h2></h2>
	<section class="container">
	<div>
		<h3 style="margin-left: 160px;">Complete Registration</h3>
	</div>
	<div style="margin-left: 150px;">
		<img src="${imgSource}" alt="..." class="img-rounded"
			style="height: 140px; width: 140px">
	</div>
	<div class="login"
		Style="height: 40%; width: 55%; margin-top: -140px; margin-left: 300px; background: white; box-shadow: inset 3px 3px 3px rgba(0, 0, 0, 0.12);">
		<form id="formId" action="saveUserInfo.action" method="POST">

			<table class="table table-hover" style="margin-left: 10px">
				<tr>
					<td>
						<div>Full Name :</div>
					</td>
					<td>
						<div>
							<input type="text" id="fullName" name="fullName"
								value="${fullName}"
								<% out.println(request.getParameter("fullName")); %>
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="email">Email Address :</div>
					</td>
					<td>
						<div>
							<input type="text" id="email" name="email" value="${email}"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="company">Company :</div>
					</td>
					<td>
						<div>
							<input type="text" id="company" name="company"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="designation">Designation :</div>
					<td>
						<div>
							<input type="text" id="designation" name="designation"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="gender">Gender :</div>
					</td>
					<td>
						<div>
							<input type="radio" name="gender" value="Male"
								style="margin-left: 130px;" ${gender=='male' ? 'checked':''}>Male<br>
							<input type="radio" name="gender" value="Female"
								style="margin-left: 130px;" ${gender=='female' ? 'checked':''}>Female<br>

						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="phone">Phone No :</div>
					</td>
					<td>
						<div>
							<input type="text" id="phone" name="phone"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="age">Age:</div>
					</td>
					<td>
						<div>
							<input type="text" id="age" name="age"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="location">Location:</div>
					</td>
					<td>
						<div>
							<input type="text" id="location" name="location"
								style="margin-left: 130px; border-radius: 4px 0px 0px 4px ! important;" />
						</div>
					</td>
				</tr>
			</table>
			<input type="hidden" name="accessToken" value="${accessToken}">
		</form>
	</div>
	<div>
		<input type="button" id="submit" value="Complete Registration"
			style="margin-left: 50%; margin-top: 10px; border-radius: 4px 0px 0px 4px; text-align: center; height: 30px; width: 30%;"
			onclick="submitForm()" />
	</div>
	</section>
</body>
<script>
function submitForm(){
	$('#formId').submit();
}
/*$(document).ready(function(){
	 var checkGender = '${gender}';
	 if(checkGender == "Male"){
		 $('input:radio[name=gender][value=Male]').click();
	 }
	 else{
		 $('input:radio[name=gender][value=Female]').click();
	 }
 }); */

</script>
</html>