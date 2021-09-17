<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<title>Delete a User</title>
</head>
<body class="bg">
<nav class="navbar navbar-dark bg-dark fixed-top">
  <div class="container-fluid">
    <a class="navbar-brand" href="#"><img alt="hello game" src="images/icon.jpg"/></a>
    <button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
      <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="offcanvasNavbarLabel">Close</h5>
        <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body">
        <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="offcanvasNavbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Games
            </a>
            <ul class="dropdown-menu" aria-labelledby="offcanvasNavbarDropdown">
              	<li><a class="dropdown-item" href="findgames">Find a Game</a></li>
    			<li><a class="dropdown-item" href="gamecreate">Create a Game</a></li>
    			<li><a class="dropdown-item" href="gameupdate">Update a Game</a></li>
    			<li><a class="dropdown-item" href="gamedelete">Delete a Game</a></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="offcanvasNavbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Users
            </a>
            <ul class="dropdown-menu" aria-labelledby="offcanvasNavbarDropdown">
              <li><a class="dropdown-item" href="usercreate">Create a User</a></li>
		      <li><a class="dropdown-item" href="userupdate">Update a User</a></li>
		      <li><a class="dropdown-item" href="userdelete">Delete a User</a></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="offcanvasNavbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Reviews
            </a>
            <ul class="dropdown-menu" aria-labelledby="offcanvasNavbarDropdown">
              <li><a class="dropdown-item" href="userreviewcreate">Add a Game Review</a></li>
              <li><a class="dropdown-item" href="userreviewdelete">Delete a Game Review</a></li>
            </ul>
          </li>
        </ul>
        <br>
      </div>
    </div>
  </div>
</nav>
	<br>
	<br>
	<br>
    <div class="container">
	<div class="row">
	<h1>Delete a User</h1>
	<form action="userdelete" method="post">
		<p>
			<div <c:if test="${messages.disableSubmit}">style="display:none"</c:if>>
				<label for="username" class="h3">UserName</label>
				<input placeholder="Type the user name that you want to delete" class="form-control border border-dark" id="username" name="username" value="${fn:escapeXml(param.username)}">
			</div>
		</p>
		<p>
			<span id="submitButton" <c:if test="${messages.disableSubmit}">style="display:none"</c:if>>
			<input class="btn btn-primary" type="submit">
			</span>
		</p>
	</form>
	<br/><br/>
	</div>
	</div>	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
       <script src="js/bootstrap.min.js"></script>
</body>
</html>
