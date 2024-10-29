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
		
			<div class="pass-each-list">
				<a href="${pageContext.request.contextPath}/TopPageServlet" class="todo">ToDoリスト</a>
				<a href="${pageContext.request.contextPath}/todo/journalList.jsp" class="journal">ジャーナルリスト</a>
			</div>

	</body>
</html>