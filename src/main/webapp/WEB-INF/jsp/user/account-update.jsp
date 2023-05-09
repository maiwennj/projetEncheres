<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%
	List<String> errors = (List<String>) request.getAttribute("errors");
	User user = (User)session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Modifier mon profil</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/assets/css/materia.css" >
<!-- 	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous"> -->
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
									<h2>Modifier mon profil</h2>
								</div>
								
								<div class="form-group row">
									<label class="col-sm-2 col-form-label " for="username">Pseudo: *</label>
									<div class="col-sm-4">
										<input type="text" id="username" name="username" 
											value="<%= user.getUsername()!=null?user.getUsername():""%>" 
											class="valid form-control" pattern="[A-Za-z0-9]{3,30}" required>
									</div>
									
									<label class="col-sm-2 col-form-label"for="lastname">Nom: *</label>
									<div class="col-sm-4">
										<input type="text"  id="lastname" name="lastname" 
											value="<%= user.getLastName()!=null?user.getLastName():"" %>" 
											class=" valid form-control" required>
									</div>
								
									<label class="col-sm-2 col-form-label"for="firstname">Prénom: *</label>	
									<div class="col-sm-4">
										<input type="text" id="firstname" name="firstname" 
											value="<%= user.getFirstName()!=null?user.getFirstName():"" %>"   
											class="valid form-control" required>
									</div>
									
									<label class="col-sm-2 col-form-label"for="email">Email: *</label>
									<div class="col-sm-4">
										<input type="email" id="email" name="email"
											value="<%= user.getEmail()!=null?user.getEmail():"" %>"  
											class="valid form-control"  pattern="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" required>
									</div>
									
									<label class="col-sm-2 col-form-label" for="phone-number">Téléphone: *</label>
									<div class="col-sm-4">
										<input type="text" id="phone-number" name="phone-number"
											value="<%= user.getPhoneNumber()!=null?user.getPhoneNumber():"" %>"  
											class="valid form-control" pattern="[0-9]{10}" required>
									</div>
									
									<label class="col-sm-2 col-form-label" for="street">Rue: *</label>
									<div class="col-sm-4">
										<input type="text" id="street" name="street" 
											value="<%= user.getStreet()!=null?user.getStreet():"" %>" 
											class="valid form-control"  required>
									</div>
									
									<label class="col-sm-2 col-form-label" for="post-code">Code postal: *</label>
									<div class="col-sm-4">
										<input type="text" id="post-code" name="post-code" 
											value="<%= user.getPostCode()!=null?user.getPostCode():"" %>" 
											class="valid form-control" pattern="^(([0-8][0-9])|(9[0-5])|(2[ab]))[0-9]{3}$" required>
									</div>
									
									<label class="col-sm-2 col-form-label"for="city">Ville: *</label>
									<div class="col-sm-4">
										<input type="text" id="city" name="city" 
											value="<%= user.getCity()!=null?user.getCity():"" %>"
											class="valid form-control" required>
									</div>
									
									<label class="col-sm-2 col-form-label"for="old-password">Mot de passe actuel: *</label>
									<div class="col-sm-10">
										<input type="password" id="old-password" name="old-password"
											class="form-control"  required>
									</div>
									
									<label class="col-sm-2 col-form-label"for="new-password">Nouveau mot de passe: *</label>
									<div class="col-sm-4">
										<input type="password" id="new-password" name="new-password"
											class="form-control"  required>
									</div>
									
									<label class="col-sm-2 col-form-label" for="new-password-confirmation">Confirmation: *</label>
									<div class="col-sm-4">
										<input type="password" id="new-password-confirmation" name="new-password-confirmation" 
											class="form-control" required>
									</div>
									
									<div class="blockquote-footer ">
										<p class="text-right">* champs obligatoire</p>
									</div>
									
									<div class="">
										<p>Crédit: <%= user.getCredit() %></p>
									</div>
					
									<div class="row justify-content-evenly mt-5 ">
										<div class="col-4">
											<button type="submit" class="btn btn-outline-success">Enregistrer</button>
										</div>
										<%//button trigger modal %>
										<div class="col-4">
											<button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#deleteModal">Supprimer mon compte</button>
										</div>
									</div>			
								</div>
							</fieldset>			
						</form>
						<%//modal %>
						<div class="modal fade " id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
							<div class="modal-dialog" role="document">
								<div class="modal-content border-danger">
									<div class="modal-header bg-danger">
										<h5 class="modal-title" id="deleteModalLabel">Confirmer le mot de passe</h5>
									</div>
									<form action="<%=request.getContextPath()%>/supprimer-mon-compte" method="post" >
										<div class="modal-body ">
											<label for="deletePassword">Mot de passe: </label>
											<input type="password" name="deletePassword" class="" autofocus required>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary" data-dismiss="modal">Annuler</button>
											<button type="submit" class="btn btn-success">Confirmation</button>
										</div>
									</form>
								</div>
							</div>
						</div>	
					</div>
				</div>
			</div>
			
		</main>
		
		<footer>
			<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
		</footer>
		
		<script src="https://code.jquery.com/jquery-3.6.4.min.js" integrity="sha256-oP6HI9z1XaZNBrJURtCoUT5SUnxFr8s3BzRl+cbzUq8=" crossorigin="anonymous"></script>
		<script src="https://unpkg.com/popper.js" ></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/js/bootstrap.min.js" ></script>
	</div>
</body>
</html>