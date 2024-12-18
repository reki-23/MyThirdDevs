<%@page import="javax.swing.text.html.FormSubmitEvent"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="common.TodoInfo" %>
<%@ page import="java.lang.StringBuilder" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/todoCashList.css" type="text/css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
		<script src="/MyThirdDevs/todo/js/todoCashList.js"></script>
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
			<!-- 共通の削除確認モーダル -->
			<form action="${pageContext.request.contextPath}/TodoDeleteServlet" method="POST">
				<section id="delete_confirm_modal">
					<h3>完全に削除しますか？</h3>
					<input type="submit" id="delCancel" name="delCancel" value="キャンセル" onclick="closeModal();">
					<input type="submit" id="delSubmit" name="delSubmit" value="決定">
					<input type="hidden" id="deleteType" name="deleteType" value="">
					<!-- 選択されたチェックボックスのタスクidをこの削除ボタンが押されたときにサーブレットに送信する -->
					<input type="hidden" id="selectedIds" name="selectedIds" value="">
				</section>
			</form>
			<div id="del_mask"></div>
			
			
			<!-- 検索機能 -->
			<div class="search-task">
				<form action="${pageContext.request.contextPath}/TodoSearchServlet" method="POST">
					<input type="text" name="searchWord">
					<input type="submit" name="search_button" value="検索">
				</form>
			</div>
			
			
			<!-- ページネーションを生成する際に必要なページ数やタスク数の情報を取得 -->
			<% 
				//URLパラメータに付加するページ番号
				int pageNum = 1;
			
				//ページ総数初期化
				int totalPageCount = 1;
				
				//検索語の初期化
				String searchWord = "";
				
				//現在のページ番号初期化
				int currentPage = 1;
				
				//全ページ数
				if(request.getAttribute("totalPageCount") != null){
					totalPageCount = (int)request.getAttribute("totalPageCount");
				}
				
				//現在のページ番号を取得
				if(request.getAttribute("currentPage") != null){
					currentPage = (int)request.getAttribute("currentPage");
				}
				
				//検索語を取得
				if(request.getAttribute("searchWord") != null){
					searchWord = (String)request.getAttribute("searchWord");
				}
			%>
			
			<!-- 並べかえの項目を取得 -->
			<%
				//並べかえ項目
				String tHeaderParameter = "";
				if(request.getAttribute("tHeaderParameter") != null){
					tHeaderParameter = (String)request.getAttribute("tHeaderParameter");
				}
				
				//並べかえようのカウンタ
				int any_pushedCounta = 1;
				if(request.getAttribute("any_pushedCounta") != null){
					if((int)request.getAttribute("any_pushedCounta") == 1){
						any_pushedCounta = 0;						
					}
				}
			%>
			
			<!-- 一覧表示 -->
			<table class="todo-list-table">
				<thead>
					<tr>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=id&any_pushedCounta=<%=any_pushedCounta%>">No<%if(any_pushedCounta==1 && tHeaderParameter.equals("id")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("id")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=status&any_pushedCounta=<%=any_pushedCounta%>">ステータス<%if(any_pushedCounta==1 && tHeaderParameter.equals("status")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("status")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=classification&any_pushedCounta=<%=any_pushedCounta%>">分類<%if(any_pushedCounta==1 && tHeaderParameter.equals("classification")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("classification")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=task&any_pushedCounta=<%=any_pushedCounta%>">タスク名<%if(any_pushedCounta==1 && tHeaderParameter.equals("task")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("task")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=description&any_pushedCounta=<%=any_pushedCounta%>">タスク概要<%if(any_pushedCounta==1 && tHeaderParameter.equals("description")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("description")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=createDateTime&any_pushedCounta=<%=any_pushedCounta%>">作成日時<%if(any_pushedCounta==1 && tHeaderParameter.equals("createDateTime")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("createDateTime")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=updateDateTime&any_pushedCounta=<%=any_pushedCounta%>">更新日時<%if(any_pushedCounta==1 && tHeaderParameter.equals("updateDateTime")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("updateDateTime")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=creator&any_pushedCounta=<%=any_pushedCounta%>">作成者<%if(any_pushedCounta==1 && tHeaderParameter.equals("creator")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("creator")){%><span class="sort-arrow">↓</span><%} %></a></th>
					</tr>
					<tbody>
						<!-- 以下、繰り返し表示 -->
						<% List<TodoInfo> todoCashList = (List<TodoInfo>)request.getAttribute("todoCashList");
							if(todoCashList != null){
								for(TodoInfo info : todoCashList){
						%>
								<tr>
								   	<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp"><%= info.getId() %></a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
									<td><%= info.getStatus() %></td>
									<td><%= info.getClassification() %></td>
									<td><%= info.getTask() %></td>
									<td><%= info.getDescription() %></td>
									<td><%= info.getCreateDateTime().format(DateTimeFormatter.ofPattern("y/M/d H:mm:ss")) %></td>
									<td><%= info.getUpdateDateTime().format(DateTimeFormatter.ofPattern("y/M/d H:mm:ss")) %></td>
									<td><%= info.getCreator() %></td>
								</tr>
						<%
								}
						   }
						%>
					</tbody>
				</thead>
			</table>
		</main>
		<footer class="footer">
			<div class="wrapper">
				<p><small>&copy; 2024 ToDo-List</small></p>
			</div>
		</footer>
	</body>
</html>