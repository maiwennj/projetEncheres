<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Error 404</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/materia.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css"/>
</head>
<body>
	<div class="container-fluid">
		<header>
			<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
		</header>
		<main>
			<div class="section">
				<h1 class="error">404</h1>
				<div class="page">Ooops!!! La page que vous cherchez n'a pas été trouvée</div>
				<a class="back-home" href="<%= request.getContextPath()%>">retour à la page d'accueil</a>
			</div>
		</main>
		<footer>
			<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
		</footer>
	</div>
</body>
</html>