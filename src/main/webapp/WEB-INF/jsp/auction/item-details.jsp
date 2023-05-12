<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="org.eni.encheres.helpers.Flash"%>
<%@page import="org.eni.encheres.bo.Item"%>
<%@page import="org.eni.encheres.bo.ItemAllInformation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
			<%
				ItemAllInformation itemAllinfo = (ItemAllInformation) request.getAttribute("itemAllInfo");
				Boolean lastAuction = itemAllinfo.getAuction().getBid()!= null?true:false;
				Boolean isTheOwner = false;
				Boolean isTheSeller = userConnected.getNoUser()==itemAllinfo.getUser().getNoUser()?true:false;
				if(lastAuction){
					isTheOwner = userConnected.getNoUser()==itemAllinfo.getAuction().getUser().getNoUser()?true:false;
				}
				Item itemDetails = itemAllinfo.getItem();
				List<String> errors = (List<String>) request.getAttribute("errors");
				String info = Flash.getMessage("success", session);
				DateTimeFormatter currentFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
			%>
			
			
			<%// ---------------------------------- SI ETAT EN COURS -------------------------------- %>
			<%if(itemAllinfo.getItem().getState().equals("E")){ %>
				<div class="container-fluid">
						<%//----- if bid succes%>
							<%if(info!=null){ %>
								<div class="alert alert-success text-center mt-5">
								<%= info %>
								</div>
							<%} %>
						<%//------  if bid succes %>
						
						<%// --------- if errors -------------- %>
						<% if( errors!=null ) for(String error : errors){ %>
								<div class="alert alert-danger text-center mt-5">
									<%= error %>
								</div>
						<% } %>
						<%// --------- if errors -------------- %>
						
					<div class="text-center mt-5">
						<h1>Détail vente</h1>
					</div>
					<div class="row">
						<div class="col">
							<img alt="article vendu" src="<%=request.getContextPath()%>/assets/images-item/300x250.svg">
						</div>
						<div class="col-6">
							<h2><%= itemDetails.getItemTitle() %></h2>
							<div class="row">
								<p>Description: <%= itemDetails.getDescription() %></p>
							</div>
							<div class="row">
								<p>Catégorie: <%= itemAllinfo.getCategory().getLibelle()%></p>
							</div>
							<div class="row">
								<p>Meilleure offre:</p>
								<%if(lastAuction){ %>
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
								<p>Mise à prix: <%= itemDetails.getInitialPrice()%> points</p>
							</div>
							<div class="row">
								<p>Fin de l'enchère: <%= itemDetails.getEndDate().format(currentFormat)%></p>
							</div>
							<div class="row">
								<p>Retrait:</p>
								<p><%= itemAllinfo.getCollectionPoint().getStreetCP()%></p>
								<p><%= itemAllinfo.getCollectionPoint().getPostCodeCP() +" "+ itemAllinfo.getCollectionPoint().getCityCP()%></p>
							</div>
							<div class="row">
								<p>Vendeur: <a href="<%=request.getContextPath()%>/profil/<%=itemAllinfo.getUser().getNoUser()%>"><%= itemAllinfo.getUser().getUsername()%></a></p>
							</div>
							<%if(!isTheSeller){ %>
								<form action="" method="post">
									<label for="offer">Ma proposition:</label>
									<input  name="offer" id="offer" type="number" <%if(isTheOwner){%>disabled=""<%} %> value="<%= itemAllinfo.getAuction().getBid()==null?itemDetails.getInitialPrice():itemAllinfo.getAuction().getBid()%>" min="<%= itemAllinfo.getAuction().getBid()==null?itemDetails.getInitialPrice()+1:itemAllinfo.getAuction().getBid()+1%>">
									<button type="submit" class="btn btn-succes <%=isTheOwner?"disabled":""%>">Enchérir</button>
								</form>
							<%} %>
						</div>
						<div class="col">
						</div>
					</div>
				</div>
				
				
			<%// ---------------------------------- SI ETAT TERMINE OU ARCHIVE -------------------------------- %>
			<%}else if(itemAllinfo.getItem().getState().equals("T") || itemAllinfo.getItem().getState().equals("A")){ %>
				<div class="container-fluid">
					<div class="text-center mt-5">
						<h1><%=isTheOwner?"Vous avez remporté la vente":!lastAuction?"Pas d'enchérisseur":itemAllinfo.getAuction().getUser().getUsername()+ " a remporté la vente"%></h1>
					</div>
					<div class="row">
						<div class="col">
							<img alt="article vendu" src="<%=request.getContextPath()%>/assets/images-item/300x250.svg">
						</div>
						<div class="col-6">
							<h2><%= itemDetails.getItemTitle() %></h2>
							<div class="row">
								<p>Description: <%= itemDetails.getDescription() %></p>
							</div>
							<div class="row">
								<p>Catégorie: <%= itemAllinfo.getCategory().getLibelle()%></p>
							</div>
							<div class="row">
								<p>Meilleure offre:</p>
								<%if(lastAuction){ %>
									<p><%=itemAllinfo.getAuction().getBid()%> pts 
										<%if(!isTheOwner){ %>par 
											<a href="<%=request.getContextPath()%>/profil/<%=itemAllinfo.getAuction().getUser().getNoUser()%>">
												<%=itemAllinfo.getAuction().getUser().getUsername()%>
											</a>
										<%} %>
								</p>
								<%}else{ %>
								<p>pas d'offre en cours.</p> 
								<%} %>
							</div>
							<div class="row">
								<p>Mise à prix: <%= itemDetails.getInitialPrice()%> points</p>
							</div>
							<div class="row">
								<p>Fin de l'enchère: <%= itemDetails.getEndDate().format(currentFormat)%></p>
							</div>
							<div class="row">
								<p>Retrait:</p>
								<p><%= itemAllinfo.getCollectionPoint().getStreetCP()%></p>
								<p><%= itemAllinfo.getCollectionPoint().getPostCodeCP() +" "+ itemAllinfo.getCollectionPoint().getCityCP()%></p>
							</div>
							<div class="row">
								<p>Vendeur: <a href="<%=request.getContextPath()%>/profil/<%=itemAllinfo.getUser().getNoUser()%>"><%= itemAllinfo.getUser().getUsername()%></a></p>
							</div>
							<%if(isTheOwner && lastAuction){ %>
								<div class="row">
									<p>Tel : <%= itemAllinfo.getUser().getPhoneNumber()%></p>
								</div>
							<%} %>
							<%if(isTheSeller && lastAuction && itemAllinfo.getItem().getState().equals("T")){ %>
								<div class="card-body">
								    <a href="<%=request.getContextPath()%>/retrait/<%=itemDetails.getNoItem()%>" class="card-link btn btn-success">Retrait effectué</a>
								</div>
							<%}else{ %>
								<div class="card-body">
								    <a href="<%=request.getContextPath()%>" class="card-link btn btn-success">Retour</a>
								</div>
							<%} %>
						</div>
						<div class="col">
						</div>
					</div>
				</div>
			<%} %>
			</main>
			<footer>
				<%@include file="/WEB-INF/jsp/parts/footer.jspf" %>
			</footer>
		</div>
	</body>
</html>