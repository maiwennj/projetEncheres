
<%@page import="org.eni.encheres.helpers.Flash"%>
<%@page import="org.eni.encheres.bo.User"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="org.eni.encheres.bo.Item"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	User user = (User)session.getAttribute("user");
	String info = Flash.getMessage("success", session);
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Accueil</title>
		<link rel="stylesheet" type="text/css" href="assets/css/materia.css">
		<link rel="stylesheet" href="assets/css/styles.css"/>
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
				<div class="text-center mt-5">
					<h1>Liste des enchères</h1>
				</div>
				<div id="search" class="col-6 mt-5 offset-3">
					<%@include file="/WEB-INF/jsp/parts/search.jspf" %>
				</div>
				<div class="mt-5">
				<%@include file="/WEB-INF/jsp/parts/results.jspf" %>
				</div>
			</main>
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
			</footer>
		</div>
	</body>
</html>