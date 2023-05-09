<%@page import="org.eni.encheres.bo.Item"%>
<%@page import="org.eni.encheres.bo.ItemAllInformation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%
	ItemAllInformation itemAllinfo = (ItemAllInformation) request.getAttribute("itemAllInfo");
	Item itemDetails = itemAllinfo.getItem();
	
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Détail vente</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/materia.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css"/>
	</head>
	<body>
		<div class="container-fluid">
			<header>
				<%@include file="/WEB-INF/jsp/parts/header.jspf" %>
			</header>
			<main>
				<div class="container-fluid">
					<div class="text-center mt-5">
						<h1>Détail vente</h1>
					</div>
					<div class="row">
						<div class="col">
							<img alt="article vendu" src="<%=request.getContextPath()%>/assets/images-item/300x250.svg">
						</div>
						<div class="col-6">
							<p><%= itemDetails.getItemTitle() %></p>
							<div class="row">
								<p>Description:</p>
								<p><%= itemDetails.getDescription() %></p>
							</div>
							<div class="row">
								<p>Catégorie:</p>
								<p><%= itemAllinfo.getCategory().getLibelle()%></p>
							</div>
							<div class="row">
								<p>Meilleure offre:</p>
								<%if(itemAllinfo.getAuction().getBid()!= null){ %>
									<p><%=itemAllinfo.getAuction().getBid()%> pts par 
									<a href="<%=request.getContextPath()%>/profil/<%=itemAllinfo.getAuction().getUser().getNoUser()%>">
										<%=itemAllinfo.getAuction().getUser().getUsername()%>
									</a>
								</p>
								<%}else{ %>
								<p>pas d'offre en cours.</p> 
								<%} %>
							</div>
							<div class="row">
								<p>Mise à prix:</p>
								<p><%= itemDetails.getInitialPrice()%> points</p>
							</div>
							<div class="row">
								<p>Fin de l'enchère:</p>
								<p><%= itemDetails.getEndDate()%></p>
							</div>
							<div class="row">
								<p>Retrait:</p>
								<p><%= itemAllinfo.getCollectionPoint().getStreetCP()%></p>
								<p><%= itemAllinfo.getCollectionPoint().getPostCodeCP() +" "+ itemAllinfo.getCollectionPoint().getCityCP()%></p>
							</div>
							<div class="row">
								<p>Vendeur:</p>
								<p><a href="<%=request.getContextPath()%>/profil/<%=itemAllinfo.getUser().getNoUser()%>"><%= itemAllinfo.getUser().getUsername()%></a></p>
							</div>
							<form action="" method="post">
								<label for="offer">Ma proposition:</label>
								<input name="offer" id="offer" type="number" value="<%= itemAllinfo.getAuction().getBid()==null?itemDetails.getInitialPrice():itemAllinfo.getAuction().getBid()%>" min="<%= itemAllinfo.getAuction().getBid()==null?itemDetails.getInitialPrice()+1:itemAllinfo.getAuction().getBid()+1%>">
								<button type="submit" class="btn btn-succes">Enchérir</button>
							</form>
						</div>
						<div class="col">
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