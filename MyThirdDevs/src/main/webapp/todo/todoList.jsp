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
			
			<!-- メッセージ表示箇所 -->
			<!-- 削除に成功した場合 -->
			<%
	        Boolean deleteJudge = (Boolean)request.getAttribute("deleteJudge");
	        %>
	        <%
	        if(deleteJudge != null && deleteJudge){
	        %>
	        	<div class = "delete-success">
	         		<h4>正常に削除されました。</h4>	
	        	</div>
	    	<%
	    	}else if(deleteJudge != null && !deleteJudge){
	    	%>
		        <div class = "delete-failure">
		        	<h4>削除するデータがありません。</h4>
		        </div>
	        <%
	        }
	        %>
			
			<!-- タスク登録完了メッセージ表示 -->
			<%if(request.getAttribute("registerMessage") != null){%>
				<div class="register-submit-message">
					<h4><%=request.getAttribute("registerMessage")%></h4>
				</div>
			<%}%>
			
			
	  		<!-- タスク登録失敗メッセージ表示 -->
			<%if(request.getAttribute("errorMessageList") != null){ %>	
				<%for(String errMsg : (List<String>)request.getAttribute("errorMessageList")){ %>
					<div class="register-faliure-message">
						<h4><%= errMsg %></h4>
					</div>
				<%} %>
			<%} %>
			
			
			<!-- 共通のエラーメッセージ表示 -->
			<%if(request.getAttribute("errorMessage") != null){ %>
				<div class="common-error-message">
					<h4><%=request.getAttribute("errorMessage")%></h4>
				</div>
			<%} %>
			
			
			<!--　タスク一括登録ボタン -->
			<form action="${pageContext.request.contextPath}/TodoRegisterServlet" method="POST" enctype="multipart/form-data"　onsubmit="validateFileUpload();" >
				<div class="bulk-register-list">
					<input type="submit" id="bulk_register" name="bulk_register" value="一括登録" disabled>
				</div>
				<input type="file" id ="csvFile" name="csvFile" accept=".csv" onchange="validateForm()">
				<input type="hidden" id="fileName" name="fileName" value="">
			</form>
	
			
			<!-- 個別登録ボタン -->
			<div class="register-list">
				<button id="register-button">個別登録</button>
			</div>
			
			
			<!-- タスクエクスポートボタン -->
			<form action="${pageContext.request.contextPath}/TodoExportServlet" method="POST" class="export-file">
				<div class="export-csvfile">
					<input type="submit" value="CSV出力">
					<input type="hidden" id="export-csv" name="export-csv">
				</div>
			</form>
			
			
			<!-- タスクフィルター付きエクスポートボタン -->
			<div class="export-filter-csv">
				<button id="export-filter-csv-button">フィルターをかけて出力</button>			
			</div>
			
			
			<!-- タスクフィルターモーダル -->
			<form action="${pageContext.request.contextPath}/TodoExportServlet" method = "POST">
				<section id = "export-csv-filter">
		   			<h1>フィルターをかけたいデータを入力してください。</h1>
		   			id:<input type = "text" name = "filter_id" id="filter_id"><br>
		   			ステータス:<select id="status" name="status">
		   					<option value=""></option>
					        <option value="未着手">未着手</option>
					        <option value="対応中">対応中</option>
					        <option value="完了">完了</option>
					        <option value="取下げ">取下げ</option>
					        <option value="保留">保留</option>
					    	</select><br>
		   			種類:<input type = "text" name = "classification"><br>
		   			タスク名:<input type = "text" name = "task"><br>
		   			タスク詳細:<input type = "text" name = "description"><br>
		   			作成日時:<input type = "text" name = "createDateTime"><br>
		   			更新日時<input type = "text" name = "updateDateTime"><br>
		   			作成者:<input type = "text" name = "creator"><br>
		   			<input type = "submit" id = "filter_submit" name = "filter_submit" value = "CSV出力する" onclick="hiddenFilterModal()">
					<input type = "submit" id = "filter_cancel" name = "filter_cancel" value = "キャンセル">
		   		</section>
			</form>
			<div id="export-csv-filter-mask"></div>
			
			
			<!-- タスク登録モーダル -->
			<form action="${pageContext.request.contextPath}/TodoRegisterServlet" method = "POST">
		   		<section id = "register_task">
		   			<h1>登録したいタスクの詳細を入力してください。</h1>
		   			id:<input type = "text" name = "id" id="id"><br>
		   			ステータス:<select id="status" name="status">
		   					<option value=""></option>
					        <option value="未着手">未着手</option>
					        <option value="対応中">対応中</option>
					        <option value="完了">完了</option>
					        <option value="取下げ">取下げ</option>
					        <option value="保留">保留</option>
					    	</select><br>
		   			種類:<input type = "text" name = "classification"><br>
		   			タスク名:<input type = "text" name = "task"><br>
		   			タスク詳細:<input type = "text" name = "description"><br>
		   			作成者:<input type = "text" name = "creator"><br>
		   			<input type = "submit" id = "register_cancel" name = "register_cancel" value = "キャンセル">
					<input type = "submit" id = "register_submit" name = "register_submit" value = "登録する">
		   		</section>
			</form>
			<div id="register_mask"></div>
			
			
			<!-- 個別削除ボタン -->
			<div class="delete-individual-list">
				<input type="submit" id="indi_submit" value="選択したタスクを削除する" onclick="setDeleteTypeAndModalDisp('individual');" disabled>
			</div>
			
			
			<!-- 一括削除ボタン -->
			<div class="delete-bulk-list">
				<input type="submit" id="bulk_submit" value="一括削除する" onclick="setDeleteTypeAndModalDisp('bulk');">
			</div>
			
			
			<!-- 共通の削除確認モーダル -->
			<form action="${pageContext.request.contextPath}/TodoDeleteServlet" method="POST">
				<section id="delete_confirm_modal">
					<h3>削除しますか？</h3>
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
						<!-- 以下、繰り返し表示 -->
						<% List<TodoInfo> todoList = (List<TodoInfo>)request.getAttribute("todoList");
							if(todoList != null){
								for(TodoInfo info : todoList){
						%>
								<tr>
								   	<td><a href="${pageContext.request.contextPath}/todo/todoDetail.jsp"><%= info.getId() %></a></td> <!-- 押下するとそのタスクだけが表示されるタスク詳細画面へ遷移する -->
									<td><%= info.getStatus() %></td>
									<td><%= info.getClassification() %></td>
									<td><%= info.getTask() %></td>
									<td><%= info.getDescription() %></td>
									<td><%= info.getCreateDateTime() %></td>
									<td><%= info.getUpdateDateTime() %></td>
									<td><%= info.getCreator() %></td>
									<td>
										<!-- 個別削除ボタン＝削除識別子は一意であるタスクNoと同値とする -->
										<form action="${pageContext.request.contextPath}/TodoDeleteServlet" method="POST">
											<input type="checkbox" name="individualDeleteId" value="<%= info.getId() %>" onchange="submitFormOnCheck(this)">
										</form>
									</td>
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