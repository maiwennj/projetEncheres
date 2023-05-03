<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% 
	List<String> errors = (List<String>) request.getAttribute("errors");

%>


<html>
	<head>
		<meta charset="UTF-8">
		<title>Connexion</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/assets/css/materia.css">
		<link rel="stylesheet" href="<%=request.getContextPath() %>/assets/css/styles.css"/>
	</head>
	<body>
		<div class="container-fluid">
			<header>
				<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
			</header>
		
			<main class="mt-5">
				<div class="row justify-content-center">
					<div class="col-6 offset-3 ">
						<div class="registration mx-auto d-block w-100">
						
							<%// --------- field errors -------------- %>
							<% if( errors!=null ) for(String error : errors){ %>
									<div class="alert alert-danger">
										<%= error %>
									</div>
							<% } %>
							<%// --------- field errors -------------- %>
						
							<form  method="post" action="" class="form-validate form-horizontal well">
								<div class="form-group row">
									<div class="row mt-5">
										<label class="col-sm-2 col-form-label " for="username">Nom d'utilisateur</label>
										<div class="col-sm-4">
											<input  class="form-control"  id="username" name="username" type="text">
										</div>
									
									</div>
									<div class="row mt-5">
										<label  class="col-sm-2 col-form-label " for="password">Mot de passe</label>
										<div class="col-sm-4">
											<input  class="form-control" id="password" name="password" type="password">
										</div>
									
									</div>
	
									<div class=" mt-5">
										<button  class="btn btn-outline-success" type="submit">Se connecter</button>
									</div>
								</div>
							</form>
							
							
						</div>
						<div class="mt-5 ">
								<a class="btn btn-outline-warning btn-lg" href="<%= request.getContextPath()%>/inscription">Créer un compte</a>			
						</div>
					</div>
				</div>
			</main>
			
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
			</footer>
		</div>
	</body>
</html>