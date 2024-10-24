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
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">2</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>完了</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr>
						<tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
						</tr><tr>
							<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp">3</a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
							<td>未着手</td>
							<td>仕事</td>
							<td>Activity作成</td>
							<td>添付ファイルを削除するActivityを作成する</td>
							<td>2024/10/25 7:30</td>
							<td>2024/10/25 13:31</td>
							<td>reki</td>
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