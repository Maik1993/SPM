<!-- Hilfeseite http://www.chartjs.org/docs/latest/charts/doughnut.html -->
<%@page import="java.util.ArrayList" %>
<%@page import="servlet.Product" %>
<% 	
 	String username = (String) session.getAttribute("name"); 
 	
	if(username == null){
		response.sendRedirect("login.jsp");
 	}
	
	int place = 1;
	ArrayList<Product> top5 = (ArrayList<Product>) request.getAttribute("top5Artikel");
	ArrayList<Product> maenneranteil = (ArrayList<Product>) request.getAttribute("maenneranteil");
	ArrayList<Product> frauenanteil = (ArrayList<Product>) request.getAttribute("frauenanteil");
	ArrayList<Product> kinderanteil = (ArrayList<Product>) request.getAttribute("kinderanteil");
	ArrayList<Product> berufsanteil = (ArrayList<Product>) request.getAttribute("berufsanteil");
	String zusammengekaufteWaren = (String)request.getAttribute("zusammengekaufteWaren");
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

<!-- 	Chart js -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.min.js"></script>
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
			<h1>Statistiken</h1>
		</div>
		<br />
		<br/>

		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-header">
						<strong>Top 5 Artikel</strong> <small> </small>
					</div>
					<div class="card-block">
						

					<div class="chart-container" style="position: relative; width:55vw">
						<canvas id="myChart"></canvas>
					</div>
					<script>
					var ctx = document.getElementById("myChart");
					var data = {
				    	    datasets: [{
				    	        data:
				    	        	[
				    	        	<% for (Product topProducts : top5) { %>
				    	        		<%= topProducts.amount() %>, 
				    	        	<% } %>
				    	        	],
				    	        backgroundColor: [
				                    'rgba(255, 99, 132, 0.2)',
				                    'rgba(54, 162, 235, 0.2)',
				                    'rgba(255, 206, 86, 0.2)',
				                    'rgba(75, 192, 192, 0.2)',
				                    'rgba(153, 102, 255, 0.2)',
				                    'rgba(255, 159, 64, 0.2)'
				                ],
				                borderColor: [
				                    'rgba(255,99,132,1)',
				                    'rgba(54, 162, 235, 1)',
				                    'rgba(255, 206, 86, 1)',
				                    'rgba(75, 192, 192, 1)',
				                    'rgba(153, 102, 255, 1)',
				                    'rgba(255, 159, 64, 1)'
				                ],
				                borderWidth: 1
				    	    
				    	    }],

				    	    // These labels appear in the legend and in the tooltips when hovering different arcs
				    	    labels: [
				    	    	<% for (Product topProducts : top5) { %>
				    	    		"<%= topProducts.title() %>",
				    	    	<% } %>
				    	    ]
				    	}
					var myChart = new Chart(ctx, {
					    type: 'pie',
					    data : data
					});
					</script>
					<br/>
					</div>
				</div>
				<br/><br/>
				<div class="card">
					<div class="card-header">
						<strong>Zusammengekaufte Artikel</strong> <small> </small>
					</div>
					<div class="card-block">
						<div style="padding: 20px;">
							<%= zusammengekaufteWaren %>
						</div>
					</div>
				</div>
				<br/><br/>
				<div class="card">
					<div class="card-header">
						<strong>Geschlechter Anteil</strong> <small> </small>
					</div>
					<div class="card-block">
						<div style="padding: 20px;">
							Männeranteil
							<% for (Product anteil : maenneranteil) { %>
		    	        		<%= anteil.amount() %>
		    	        	<% } %>
		    	        	<br/>
		    	        	Frauenanteil
							<% for (Product anteil : frauenanteil) { %>
		    	        		<%= anteil.amount() %>
		    	        	<% } %>		    	        	
						</div>
					</div>
				</div>
				<br/><br/>
				<div class="card">
					<div class="card-header">
						<strong>Weitere Information zu den Personen</strong> <small> </small>
					</div>
					<div class="card-block">
						<div style="padding: 20px;">
							Besitzen Kinder
							<% for (Product anteil : kinderanteil) { %>
		    	        		<%= anteil.amount() %>
		    	        	<% } %>
		    	        	<br/>	  
		    	        	Berufstätig
							<% for (Product anteil : berufsanteil) { %>
		    	        		<%= anteil.amount() %>
		    	        	<% } %>  	
		    	        	<br/>        	
						</div>
					</div>
				</div>
		</div>
		<br/>
		<br/>

	</div>
	</div>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
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
		// Any of the following formats may be used
		var ctx = document.getElementById("myChart");
		var ctx = document.getElementById("myChart").getContext("2d");
		var ctx = $("#myChart");
		var ctx = "myChart";
		</script>
</body>
</html>