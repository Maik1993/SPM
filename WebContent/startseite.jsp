<% 	
 	String username = (String) session.getAttribute("name"); 
	System.out.println(username);
 	
	if(username == null){
		response.sendRedirect("login.jsp");
 	}
%>

<!DOCTYPE html>
<html>
<head>
  	<title>Just Do IT</title>
  	<meta charset="utf-8">

  	<link href="css/bootstrap.min.css" rel="stylesheet">
  
	<!-- Unsere eigenen Styles -->
	<link href="styles.css" rel="stylesheet">
	
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div id="image">
					<img id="logo_img" src="images/Logo_transparent.png">
				</div>
			</div>
			<div class="col-md-6">
				<div id="ueberschrift">
					<h1>Willkommen User <%= username %></h1>
				</div>			
			</div>
			<div class="col-md-3">
				<form action="Logout" method="POST">
					<input type="submit" id="login_button" value="Logout" class="btn btn-primary btn-lg btn-block login-button">
				</form>
			</div>		
		</div>
		<br/>
		<br/>
	</div>


	
	
	<footer>
			<div class="container">
			<div class="row">
				<div class="col-md-9">
					<p>&copy; Just Do IT</p>
					<p>David Boes, Christopher Heyn, Justine L&uuml;ken, Lars Lipport, Maik Habben, Ina Krefting</p>
				</div>
				<div class="col-md-3">
					<img id="logo_gruppe_img" src="images/Just_do_it_logo.png">
				</div>
				</div>
			</div>
	</footer>
</body>
</html>