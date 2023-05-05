<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String itemTitle = request.getParameter("item-title");
Category searchedCategory = (Category) request.getAttribute("category");
String libelleCategory = searchedCategory.getLibelle();
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Recherche</title>
		<link rel="stylesheet" type="text/css" href="assets/css/materia.css">
		<link rel="stylesheet" href="assets/css/styles.css"/>
	</head>
	<body>
		<div class="container-fluid">
			<header>
				<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
			</header>
			<main>
				<div class="text-center mt-5">
					<h1>Résultat de recherche</h1>
				</div>
				<div id="search" class="col-6 mt-5 offset-3">
					<%@include file="/WEB-INF/jsp/parts/search.jspf" %>
				</div>
				<div class="mt-2">
					<div class="col-6 mt-2 offset-3">
						<div><strong>Recherche effectuée :</strong></div>
						<div>titre = <%=(itemTitle.isEmpty()?"*":itemTitle) %></div>
						<div>catégorie = <%=(libelleCategory==null )?"toutes":libelleCategory %></div>
					</div>
					<%@include file="/WEB-INF/jsp/parts/results.jspf" %>
				</div>
			</main>
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
			</footer>
		</div>
	</body>
</html>