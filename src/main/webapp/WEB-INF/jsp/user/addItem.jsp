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
 	String initialePrice=request.getParameter("initialePrice");
 	String startDate=request.getParameter("startDate");
 	String endDate= request.getParameter("endDate");
 	List<Category> listCategories = (List<Category>)request.getAttribute("listCategories");
//  	Item itemToSell = (Item) request.getAttribute("item"); 
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
			<h1  center> Nouvelle vente </h1>
			<div>
			<label for="item-title" class="form-label mt-4">Article</label>
			<input type="text" class="form-control" 
			id="item-title" name="item-title" value="<%=(itemTitle!=null)? itemTitle:"" %>" >  
	
			<label for="description" class="form-label mt-4">Description</label>
			<input type="text" class="form-control" 
			id="description" name="description" value="<%=(description!=null)? description:"" %>" >  

			<div class="form-group">
		      <label for="category" class="form-label mt-4">Catégorie :</label>
		      <select class="form-select" id="category" name="category">
		      	<option value="0">Toutes</option>
		        <% for (Category categoryFromList : listCategories){ %>
		        <option value="<%=categoryFromList.getNoCategory() %>"><%=categoryFromList.getLibelle() %></option>
		        <%} %>
		      </select>
		    </div> 
		
			<label for="initialePrice" class="form-label mt-4">Mise à prix</label>
			<input type="number" class="form-control" 
			id="initialePrice" name="initialePrice"  value="<%=(initialePrice!=null)? initialePrice:"" %>" >  

			<label for="startDate" class="form-label mt-4">Début de l'enchère </label>
			<input type="LocalDate" class="form-control" 
			id="startDate" name="startDate"  value="<%=(startDate!=null)? startDate:"" %>" >  
	
			<label for="endDate" class="form-label mt-4">Fin de l'enchère </label>
			<input type="LocalDateTime" class="form-control" 
			id="endDate" name="endDate"  value="<%=(endDate!=null)? endDate:"" %>" >  
			</div>
	<div>
	<fieldset>
		<legend>Retrait</legend>
		<div class="form-group">
			<label for="streetCP" class="form-label mt-4">Rue</label>
			<input type="text" class="form-control" id="streetCP" name="streetCP" value="<%=userConnected.getStreet()%>">
		</div>
		<div class="form-group">
			<label for="postCodeCP" class="form-label mt-4" >Code Postal</label>
			<input type="text" class="form-control" id="postCodeCP" name="postCodeCP" value="<%=userConnected.getPostCode() %>">
		</div>
		<div class="form-group">
			<label for="cityCP" class="form-label mt-4" >Ville</label>
			<input type="text" class="form-control" id="cityCP" name="cityCP" value="<%=userConnected.getCity() %>">
		</div>
	</fieldset>
	</div>	
		<div>
		<button type="submit">Enregistrer</button>
		<button type="reset">Annuler</button>
		</div>
</body>
</html>