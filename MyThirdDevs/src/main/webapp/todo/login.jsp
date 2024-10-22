<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/login.css" type="text/css">
		<title>ログイン画面</title>
	</head>
	<body>
		<h1>ログイン画面</h1>
		<div class = error>
			<% if((String)request.getAttribute("loginError") != null) {%>
				<h3><%= (String)request.getAttribute("loginError") %></h3>
			<%} %>
		</div>
		<form action = "${pageContext.request.contextPath}/LoginServlet" method = "POST">
			<div class = "user">
				<span class = "required">ユーザー名</span><br>
				<input type = "text" name = "user"><br>
			</div>
			<div class = "pass">
				<span class = "required">パスワード</span><br>
				<input type = "password" name = "pass"><br>
			</div>
			<div class = "login">
				<input type = "submit" name = "login" value = "ログイン">
			</div>
		</form>
	</body>
</html>