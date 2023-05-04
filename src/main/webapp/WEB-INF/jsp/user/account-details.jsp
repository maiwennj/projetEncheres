
<%@page import="org.eni.encheres.helpers.Flash"%>
<%@page import="org.eni.encheres.bo.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User requestUser = (User) request.getAttribute("user"); 
	User sessionUser = (User) session.getAttribute("user");
	String info = Flash.getMessage("success", session);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Profil</title>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/assets/css/materia.css">
		<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css"/>
	</head>
	<body>
		<div class="container-fluid">
			<header>
				<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
			</header>
			<main>
				<%//----- if account created%>
					<%if(info!=null){ %>
						<div class="alert alert-success text-center mt-5">
						<%= info %>
						</div>
					<%} %>
				<%//------  if account created %>
				<div class="row mt-5 text-center">
					<p>Pseudo: <%=requestUser.getUsername()%></p>
					<p>Nom: <%= requestUser.getFirstName() %></p>
					<p>Prenom: <%= requestUser.getLastName() %></p>
					<p>Email: <%= requestUser.getEmail() %></p>
					<p>Telephone: <%= requestUser.getPhoneNumber() %></p>
					<p>Rue: <%= requestUser.getStreet()%> </p>
					<p>Code Postal: <%= requestUser.getPostCode() %></p>
					<p>Ville: <%= requestUser.getCity()%></p>
				</div>
				<%if(requestUser.getNoUser() == sessionUser.getNoUser()){ %>
					<div class="mt-5 text-center">
						<a class="btn btn-warning btn-lg" href="<%=request.getContextPath()%>/modifier-profil">Modifier</a>
					</div>
				<%}%>
			</main>
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf"%>
			</footer>
		</div>
	</body>
</html>