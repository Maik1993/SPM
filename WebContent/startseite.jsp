<!-- https://stackoverflow.com/questions/19525608/how-to-include-loading-gif-while-file-upload-and-insert-to-database-is-in-progre -->
<% 	
 	String username = (String) session.getAttribute("name"); 
 	
	if(username == null){
		response.sendRedirect("login.jsp");
 	}
	
	boolean noData = false;
	try {
		noData = (boolean) session.getAttribute("noData");
	} catch (NullPointerException e) {

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
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://v4-alpha.getbootstrap.com/dist/js/bootstrap.min.js"></script>
    
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

	<script>
	$(document).ready(function(){
		var loading = $('#loading');
		loading.hide();
		
	    $.fn.showLoading = function(){ 
	    	if(loading.css('display') == 'none') {
	    		loading.show();
	    		}
	    };
	    
	    $.fn.hideLoading = function(){ 
	    	if(loading.css('display') == 'block') {
	    		loading.hide();
			};
	    };
	});

	</script>

</head>
<body>

	<nav class="navbar navbar-expand-md navbar-light bg-light">
		<a class="navbar-brand" href="#"> <img id="navbar_logo_img"
			src="images/Logo_transparent.png">
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarText" aria-controls="navbarText"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarText">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item"><a class="nav-link" href="startseite.jsp"><span class="fa fa-file-excel-o"></span> Daten Analyse</a>
				</li>
				<li class="nav-item"><a class="nav-link" href="statistik.jsp"><span class="fa fa-bar-chart"></span> Statistiken</a>
				</li>
			</ul>
			<form action="Logout" method="POST">
			<span class="navbar-text"> 		
       			<button id="logout_button" type="submit" class="btn btn-primary btn-lg btn-block login-button">
     					<span class="fa fa-sign-out"></span> Logout
   				</button>
			</span>
			</form>
		</div>
	</nav>
	<br>


	<div class="container">
	
		<div id="ueberschrift">
			<h1>Daten Analyse</h1>
		</div>
		<br />
		<br/>

		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-header">
						<strong>Wählen Sie Ihre CSV Datei aus</strong> <small> </small>
					</div>
					<div class="card-block">
						<div class="upload_body">
						<br/>
						<br/>
						    
						    <form method="post" enctype="multipart/form-data" action="FileUploader">
								<input type="file" id="file" name="file" class="inputfile" accept=".csv">
								<br/><br/>
							<% if(noData){ %>
							<br/>
							<p class="text-danger">Keine Datei ausgewählt!</p>
						
							<% } %>
								<div id="analyse_button">
									<input type="submit" id="button" value="Analysieren" class="btn btn-primary btn-lg btn-block analyse-button">
								</div>
							</form>
						    <img id="loading" src="images/loading_icon.gif" width="250px" style="display: none">
						</div>
					</div>
					<br/>
				</div>
			</div>
		</div>
		<br/>
		<br/>

	</div>
		<br/>
		<br/>
		<br/>
		<br/>
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
	
		<script>
		$("#button").click(function () {     
		    //call show loading function here
		    $.fn.showLoading();
		});
		
		</script>
</body>
</html>