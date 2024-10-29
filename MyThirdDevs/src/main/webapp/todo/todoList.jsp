<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="common.TodoInfo" %>
 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/todoList.css" type="text/css">
		<script src="/MyThirdDevs/todo/js/todoList.js"></script>
		<title>TODOリスト</title>
	</head>
	<body>
		<header>
			<div class="logo">
				<h2>TODOリスト</h2>			
			</div>
			<nav>
				<ul>
					<li><a href="${pageContext.request.contextPath}/todo/topPage.jsp">トップ</a></li>
					<li><a href="${pageContext.request.contextPath}/todo/journalList.jsp">ジャーナル</a></li>
					<li><a href="${pageContext.request.contextPath}/todo/logout.jsp">ログアウト</a></li>
				</ul>
			</nav>		
		</header>
		
		<main>
		
		
		<!-- 登録ボタン -->
			<div class="register-list">
				<button id="register-button">タスクを登録する</button>
			</div>
			
			<!-- タスク登録モーダル -->
			<form action="${pageContext.request.contextPath}/TodoRegisterServlet" method = "POST">
		   		<section id = "register_task">
		   			<h1>登録したいタスクの詳細を入力してください。</h1>
		   			id:<input type = "text" name = "id"><br>
		   			ステータス:<input type = "text" name = "status"><br>
		   			種類:<input type = "text" name = "classification"><br>
		   			タスク名:<input type = "text" name = "task"><br>
		   			タスク詳細:<input type = "text" name = "description"><br>
		   			作成者:<input type = "text" name = "creator"><br>
		   			<input type = "submit" id = "register_cancel" name = "register_cancel" value = "キャンセル">
					<input type = "submit" id = "register_submit" name = "register_submit" value = "登録する">
		   		</section>
			</form>
			<div id="register_mask"></div>
			
		<!-- 削除ボタン -->
			<div class="delete-bulk-list">
				<form action="${pageContext.request.contextPath}/TodoDeleteServlet" method="POST">
					<input type="submit" value="一括削除する">
					<input type="hidden" name="deleteTodo" id="deleteTodo">
				</form>
			</div>

		<!-- 個別削除ボタン -->
		<!-- 個別削除は、テーブルの右端に削除ボタンを配置し、削除を押下したら共通の削除確認モーダルを表示する -->
			<div class="delete-each-list">
				<form action="${pageContext.request.contextPath}/TodoDeleteServlet" method="POST">
					<input type="submit" value="選択して削除する">
					<input type="hidden" name="deleteTodo" id="deleteTodo">
				</form>
			</div>
			
			<!-- 一覧表示 -->
			<table class="todo-list-table">
				<thead>
					<tr>
						<th>No</th>
						<th>ステータス</th>
						<th>分類</th>
						<th>タスク名</th>
						<th>タスク概要</th>
						<th>作成日時</th>
						<th>更新日時</th>
						<th>作成者</th>
						<th>削除</th>					
					</tr>
					<tbody>
						<tr>
						<!-- 以下、繰り返し表示 -->
						<% List<TodoInfo> todoList = (List<TodoInfo>)request.getAttribute("todoList");
							if(todoList != null){
								for(TodoInfo info : todoList){
						%>
								   	<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp"><%= info.getId() %></a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
									<td><%= info.getStatus() %></td>
									<td><%= info.getClassification() %></td>
									<td><%= info.getTask() %></td>
									<td><%= info.getDescription() %></td>
									<td><%= info.getCreateDateTime() %></td>
									<td><%= info.getUpdateDateTime() %></td>
									<td><%= info.getCreator() %></td>
									<td>□</td> <!-- 実際はチェックボックスかラジオボタン -->
						<%    
								}
						   }
						%>
							
						</tr>
					</tbody>
				</thead>
			</table>
		</main>
		<footer>
			<div class="wrapper">
				<p><small>&copy; 2024 ToDo-List</small></p>
			</div>
		</footer>
	</body>
</html>