<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%
	List<String> errors = (List<String>) request.getAttribute("errors");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Inscription</title>
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
				<div class="row">
					<div class="col-6 offset-3 ">
						<div class="registration mx-auto d-block w-100">
							<%// --------- field errors -------------- %>
							<% if( errors!=null ) for(String error : errors){ %>
									<div class="alert alert-danger">
										<%= error %>
									</div>
							<% } %>
							<%// --------- field errors -------------- %>

							<form id="user-registration" action="" method="post"  class="form-validate form-horizontal well">
								
								<fieldset>
									<div class="page-header text-center">
										<h2>Créer mon profil</h2>
									</div>
									
									<div class="form-group row">
										<label class="col-sm-2 col-form-label " for="username">Pseudo: *</label>
										<div class="col-sm-4">
											<input type="text" id="username" name="username" 
												value="<%=(request.getParameter("username")!=null)?request.getParameter("username"):"" %>" 
												class="valid form-control" pattern="^[A-Za-z][A-Za-z0-9_]{3,30}$" required>
										</div>
										
										<label class="col-sm-2 col-form-label"for="lastname">Nom: *</label>
										<div class="col-sm-4">
											<input type="text"  id="lastname" name="lastname" 
												value="<%=(request.getParameter("lastname")!=null)?request.getParameter("lastname"):"" %>" 
												class=" valid form-control-plaintext" required>
										</div>
									
										<label class="col-sm-2 col-form-label"for="firstname">Prénom: *</label>	
										<div class="col-sm-4">
											<input type="text" id="firstname" name="firstname" 
												value="<%=(request.getParameter("firstname")!=null)?request.getParameter("firstname"):"" %>"   
												class="valid form-control" required>
										</div>
										
										<label class="col-sm-2 col-form-label"for="email">Email: *</label>
										<div class="col-sm-4">
											<input type="email" id="email" name="email"
												value="<%=(request.getParameter("email")!=null)?request.getParameter("email"):"" %>"  
												class="valid form-control"  pattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" required>
										</div>
										
										<label class="col-sm-2 col-form-label" for="phone-number">Téléphone: *</label>
										<div class="col-sm-4">
											<input type="text" id="phone-number" name="phone-number"
												value="<%=(request.getParameter("phone-number")!=null)?request.getParameter("phone-number"):"" %>"  
												class="valid form-control" pattern="[0-9]{10}" required>
										</div>
										
										<label class="col-sm-2 col-form-label" for="street">Rue: *</label>
										<div class="col-sm-4">
											<input type="text" id="street" name="street" 
												value="<%=(request.getParameter("street")!=null)?request.getParameter("street"):"" %>" 
												class="valid form-control"  required>
										</div>
										
										<label class="col-sm-2 col-form-label" for="post-code">Code postal: *</label>
										<div class="col-sm-4">
											<input type="text" id="post-code" name="post-code" 
												value="<%=(request.getParameter("post-code")!=null)?request.getParameter("post-code"):"" %>" 
												class="valid form-control" pattern="^(([0-8][0-9])|(9[0-5])|(2[ab]))[0-9]{3}$" required>
										</div>
										
										<label class="col-sm-2 col-form-label"for="city">Ville: *</label>
										<div class="col-sm-4">
											<input type="text" id="city" name="city" 
												value="<%=(request.getParameter("city")!=null)?request.getParameter("city"):"" %>"
												class="valid form-control" required>
										</div>
										
										<label class="col-sm-2 col-form-label"for="password">Mot de passe: *</label>
										<div class="col-sm-4">
											<input type="password" id="password" name="password"
												class="form-control"  required>
										</div>
										
										<label class="col-sm-2 col-form-label" for="password-confirmation">Confirmation: *</label>
										<div class="col-sm-4">
											<input type="password" id="password-confirmation" name="password-confirmation" 
												class="form-control" required>
										</div>
										
										
										<div class="blockquote-footer ">
											<p class="text-right">* champs obligatoire</p>
										</div>
						
						
										<div class="row justify-content-evenly mt-5 ">
											<div class="col-4">
												<button type="submit" class="btn btn-outline-success">Créer</button>
											</div>
											<div class="col-4">
												<a class="btn btn-outline-warning" href="<%= request.getContextPath()%>">Annuler</a>
											</div>
										</div>
										
									</div>
								</fieldset>			
							</form>
							
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