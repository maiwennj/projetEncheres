<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="org.apache.catalina.Session"%>
<%@page import="org.eni.encheres.bo.Item"%>
<%@page import="org.eni.encheres.bo.Category"%>
<%@page import="java.time.LocalDateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 	String itemTitle = request.getParameter("itemTitle");
 	String description = request.getParameter("description");
 	String category = request.getParameter("category");
 	String initialPrice=request.getParameter("initialPrice");
 	String startDate=request.getParameter("startDate");
 	String endDate= request.getParameter("endDate");
 	List<Category> listCategories = (List<Category>)request.getAttribute("listCategories");
	List<String> errors = (List<String>) request.getAttribute("errors");
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String nowDateTime = LocalDateTime.now().format(formatter);
	String inAWeekString = LocalDateTime.now().plusDays(7).format(formatter);
//     nowDateTime.replace(" ", "T");
	
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/assets/css/materia.css">
		<link rel="stylesheet" href="<%=request.getContextPath() %>/assets/css/styles.css"/>
		<title>Nouvelle Vente</title>
	</head>
	<body>
		<div class="container-fluid">
			<header>
				<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
			</header>
			<div class="page-header text-center mt-5">
				<h1>Nouvelle vente</h1>
			</div>
			<%// --------- field errors -------------- %>
				<% if( errors!=null ) for(String error : errors){ %>
						<div class="alert alert-danger">
							<%= error %>
						</div>
				<% } %>
			<%// --------- field errors -------------- %>
				<form class="col-6 offset-3" action="" method="post">
					<div class="form-group row">
						<label for="itemTitle" class="form-label mt-4">Titre de l'article : </label>
						<input type="text" class="form-control" id="itemTitle" name="itemTitle" value="<%=(itemTitle!=null)? itemTitle:"" %>" required>  
					</div>
					<div class="form-group row">
						<label for="description" class="form-label mt-4">Description :</label>
						<textarea type="text" class="form-control" id="description" name="description" rows="5" cols="50" required><%=(description!=null)? description:""%></textarea> 
					</div>
					<div class="form-group row">
				      	<label for="category" class="form-label mt-4">Catégorie :</label>
				      	<select class="form-select" id="category" name="category" required>
				      	<option value="0">Toutes</option>
				        <% for (Category categoryFromList : listCategories){ %>
				        	<%if (category!=null && Integer.parseInt(category)==categoryFromList.getNoCategory()){%>
				        		<option value="<%=categoryFromList.getNoCategory() %>" selected><%=categoryFromList.getLibelle()%></option>
							<%}else{ %>
				        		<option value="<%=categoryFromList.getNoCategory() %>"><%=categoryFromList.getLibelle()%></option>
				       		<%} %>
				        <%} %>
				      	</select>
				    </div> 
					<div class="form-group row">
						<label for="initialPrice" class="form-label mt-4">Mise à prix</label>
						<input type="number"  min="5" class="form-control" id="initialPrice" name="initialPrice"  value="<%=(initialPrice!=null)? initialPrice:"5" %>" required>  
					</div> 
					<div class="form-group row">
						<label for="startDate" class="form-label mt-4">Début de l'enchère </label>
						<input type="datetime-local" class="form-control" id="startDate" name="startDate" value="<%=(startDate!=null)?startDate:nowDateTime %>" required>  
					</div> 
					<div class="form-group row">
						<label for="endDate" class="form-label mt-4">Fin de l'enchère </label>
						<input type="datetime-local" class="form-control" id="endDate" name="endDate" value="<%=(endDate!=null)? endDate:inAWeekString %>" required>  
					</div>
					<fieldset>
						<legend>Retrait</legend>
						<div class="form-group">
							<label for="streetCP" class="form-label mt-4">Rue</label>
							<input type="text" class="form-control" id="streetCP" name="streetCP" value="<%=userConnected.getStreet()%>" required>
						</div>
						<div class="form-group">
							<label for="postCodeCP" class="form-label mt-4" >Code Postal</label>
							<input type="text" class="form-control" id="postCodeCP" name="postCodeCP" value="<%=userConnected.getPostCode() %>" required>
						</div>
						<div class="form-group">
							<label for="cityCP" class="form-label mt-4" >Ville</label>
							<input type="text" class="form-control" id="cityCP" name="cityCP" value="<%=userConnected.getCity() %>" required>
						</div>
					</fieldset>
					<div>
						<button class="btn btn-success" type="submit">Enregistrer</button>
						<a class="btn btn-warning" href="<%= request.getContextPath() %>">Annuler</a>
					</div>
				</form>
			</div>
		</body>
</html>