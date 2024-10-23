<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/topPage.css" type="text/css">
		<title>トップ</title>
	</head>
	<body>
		<h1>トップページ</h1>
		<form action = "${pageContext.request.contextPath}/TopPageServlet" method = "POST">
			<div class="pass-each-list">
				<a href="${pageContext.request.contextPath}/todo/todoList.jsp" class="todo">ToDoリストへ</a>
				<a href="${pageContext.request.contextPath}/todo/journalList.jsp" class="journal">ジャーナルリストへ</a>
			</div>
		</form>
	</body>
</html>