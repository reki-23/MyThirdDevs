<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/todoList.css" type="text/css">
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
				<form action="${pageContext.request.contextPath}/TodoRegisterServlet" method="POST">
					<input type="submit" value="登録する">
					<input type="hidden" name="registerTodo" id="registerTodo">
				</form>
			</div>
			
			
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
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">1</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>対応中</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
							<td>□</td> <!-- 実際はチェックボックスかラジオボタン -->
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