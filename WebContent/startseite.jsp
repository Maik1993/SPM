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

<% String passwort = request.getParameter("passwort"); %>
<a>Welcome   <% out.println(passwort); %> User!!!! You have logged in.</a>
</body>
</html>