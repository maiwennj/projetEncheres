
<%@page import="org.eni.encheres.bo.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% User user = (User) request.getAttribute("user"); %>

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
				<div class="row mt-5 text-center">
					<p> Pseudo:<%=user.getUsername()%></p>
					<p>Nom: <%= user.getFirstName() %><p>
					<p>Prenom: <%= user.getLastName() %>
					<p>Email: <%= user.getEmail() %>  </p>
					<p>Telephone: <%= user.getPhoneNumber() %>  </p>
					<p>Rue: <%= user.getStreet()%> </p>
					<p>Code Postal: <%= user.getPostCode() %> </p>
					<p>Ville: <%= user.getCity()%> </p>
				</div>
			</main>
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
			</footer>
		</div>
	</body>
</html>