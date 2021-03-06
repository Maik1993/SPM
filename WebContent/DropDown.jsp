<!-- https://stackoverflow.com/questions/19525608/how-to-include-loading-gif-while-file-upload-and-insert-to-database-is-in-progre -->
<%@ page import="servlet.Strategienliste, java.io.IOException" %>
<% 	
 	String username = (String) session.getAttribute("name"); 
 	
	if(username == null){
		response.sendRedirect("login.jsp");
 	}
	
	boolean noData = false;
	boolean wrongData = false;
	try {
		noData = (boolean) session.getAttribute("noData");
		wrongData = (boolean) session.getAttribute("wrongData");
	} catch (NullPointerException e) {

	}
	
	Strategienliste sl = new Strategienliste();
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
    <!-- <script src="https://v4-alpha.getbootstrap.com/dist/js/bootstrap.min.js"></script> -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

	<style type="text/css"> .card-block {padding:30px;} </style>

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
				<li class="nav-item"><a class="nav-link" href="DropDown.jsp"><span class="fa fa-file"></span> Marketing</a>
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
			<h1>Marketingstrategien</h1>
		</div>
		<br />
		<br/>

		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-header">
						<strong>Vorgeschlagene Strategien</strong> <small> </small>
					</div>
					<div class="card-block">
						<div class="dropdown">
							<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">Strategien</button>
							<ul class="dropdown-menu">
							<% for(String s:sl.getList()) {
								System.out.println(" " + s);%>
								<li><a href="#"><%=s %></a></li>
							<% } %>
							</ul>
						</div>
						<br/>
						
						<form>
						<button class="btn btn-success" type="button" data-toggle="modal" data-target="#addModal">Hinzuf&uuml;gen</button>
						<button class="btn btn-danger" type="submit" name="del" value="y">Liste l&ouml;schen</button>
						<!-- Modal -->
						<div class="modal fade" id="addModal" role="dialog">
							<div class="modal-dialog">
								<!-- Modal Content -->
								<div class="modal-content">
								
									<div class="modal-header">
										<h4 class="modal-title">Marketingstrategie hinzuf&uuml;gen</h4>
										<button type="button" class="close" data-dismiss="modal">&times;</button>
									</div>
									<div class="modal-body">
											<input class="form-control" type="text" name="strategy" />
											<%session.setAttribute("strat", request.getParameter("strategy"));
											String newStrat = (String) session.getAttribute("strat");
											System.out.println("Test: " + newStrat);
											System.out.println("del: " + request.getParameter("del")); 
											if(newStrat != null && newStrat != "null") {
												sl.addStrategy(newStrat);
											}
											if(request.getParameter("del") != null) {
											if(request.getParameter("del").equals("y")) {
												sl.delete();
												response.sendRedirect("DropDown.jsp");
											}}%>
									</div>
									<div class="modal-footer">
										
										<button type="submit" class="btn btn-default">Best&auml;tigen</button>
										<button type="button" class="btn btn-default" data-dismiss="modal">Schlie&szlig;en</button>
									</div>
								
								</div>
							</div>
						</div>
						</form>
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
				<% java.util.Date d = new java.util.Date(); %>
					<p><%=d %></p>
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
		    console.log('fired');
		});
		
		</script>
</body>
</html>