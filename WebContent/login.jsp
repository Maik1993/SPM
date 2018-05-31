<!DOCTYPE html>
<%
	boolean error = false;
	try {
		error = (boolean) session.getAttribute("error");
	} catch (NullPointerException e) {

	}
%>
<html>
<head>
<title>Just Do IT</title>
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Unsere eigenen Styles -->
<link href="styles.css" rel="stylesheet">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
	$(document).ready(function() {
		$("#login_box").fadeIn(2000);
	});
</script>
</head>
<body>

	<form method="POST" action="Login">
		<div class="container">
			<div class="row">
				<div class="col-md-4"></div>
				<div class="col-md-4">
					<div id="login_box" style="display: none">
						<div id="image">
							<img id="logo_img" src="images/Logo_transparent.png">
						</div>
						<br />
						<div id="ueberschrift_login_box">Login</div>
						<br /> <input type="password" class="form-control" name="passwort"
							placeholder="Passwort">
							<% if(error){ %>
							<br/>
							<p class="text-danger">Falsches Passwort!</p>
						
							<% } %>
							 <br /> <input type="submit" id="button" value="Einloggen"
							class="btn btn-primary btn-lg btn-block login-button">
					</div>
				</div>
				<div class="col-md-4"></div>
			</div>
			<br /> <br /> <br /> <br /> <br /> <br /> <br /> <br />
		</div>
	</form>

	<footer>
		<div class="container">
			<div class="row">
				<div class="col-md-9">
				<% java.util.Date d = new java.util.Date(); %>
					<p><%=d %></p>
					<p>&copy; Just Do IT</p>
					<p>David Boes, Christopher Heyn, Justine L&uuml;ken, Lars
						Lipport, Maik Habben, Ina Krefting</p>
				</div>
				<div class="col-md-3">
					<img id="logo_gruppe_img" src="images/Just_do_it_logo.png">
				</div>
			</div>
		</div>
	</footer>
</body>
</html>