<%@page import="javax.swing.text.html.FormSubmitEvent"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.common.TodoInfo" %>
<%@ page import="java.lang.StringBuilder" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/todoList.css" type="text/css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
		<script src="/MyThirdDevs/todo/js/todoList/delete.js"></script>
		<script src="/MyThirdDevs/todo/js/todoList/favorites.js"></script>
		<script src="/MyThirdDevs/todo/js/todoList/modal.js"></script>
		<script src="/MyThirdDevs/todo/js/todoList/register.js"></script>
		<script src="/MyThirdDevs/todo/js/utils.js"></script>
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
			<%
			if(request.getAttribute("registerMessage") != null){
			%>
				<div class="register-submit-message">
					<h4><%=request.getAttribute("registerMessage")%></h4>
				</div>
			<%
			}
			%>
			
			
			<!-- お気に入り登録完了メッセージ表示 -->
			<%
			if(request.getAttribute("favoriteSubmitMessage") != null){
			%>
				<div class="register-submit-message">
					<h4><%=(String)request.getAttribute("favoriteSubmitMessage")%></h4>
				</div>
			<%
			}
			%>
			
			
			<!-- お気に入りから削除した際のメッセージ -->
			<%
			if(request.getAttribute("favoriteCancelMessage") != null){
			%>
				<div class="register-faliure-message">
					<h4><%=(String)request.getAttribute("favoriteCancelMessage")%></h4>
				</div>
			<%
			}
			%>
			
			
	  		<!-- タスク登録失敗メッセージ表示 -->
			<%
			if(request.getAttribute("errorMessageList") != null){
			%>	
				<%
					for(String errMsg : (List<String>)request.getAttribute("errorMessageList")){
					%>
					<div class="register-faliure-message">
						<h4><%=errMsg%></h4>
					</div>
				<%
				}
				%>
			<%
			}
			%>
			
			
			<!-- 共通のエラーメッセージ表示 -->
			<%
			if(request.getAttribute("errorMessage") != null){
			%>
				<div class="common-error-message">
					<h4><%=request.getAttribute("errorMessage")%></h4>
				</div>
			<%
			}
			%>
			
			
			<!--　タスク一括登録ボタン -->
			<form action="${pageContext.request.contextPath}/TodoRegisterServlet" class="bulk-register-form" method="POST" enctype="multipart/form-data"　onsubmit="validateFileUpload();" >
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
			<form action="${pageContext.request.contextPath}/TodoExportServlet" method="POST">
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
		   			お気に入り:<input type= "checkbox" name = "isFavorite"><br>
		   			<input type = "submit" id = "filter_submit" name = "filter_submit" value = "CSV出力する" onclick="hiddenFilterModal()">
					<button type="button" id = "filter_cancel" onclick="hiddenFilterModalOnCancel()">キャンセル</button>
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
			
			<!-- ページネーション -->
			<div class="main-wrapper-pagination">
				<!-- ページ総数が3以下の場合 -->
				<%
				if(totalPageCount <= 3){
				%>
					<!-- ページ総数が1ページの場合＝常に1ページ目 -->
					<%
					if(totalPageCount == 1){
					%>
						<ul>
							<li class="current-page">&emsp;1&emsp;</li>
						</ul>
					<%
					}
					//ページ総数がNページの場合で、現在のページが1ページ目 -->
					else if(currentPage == 1 && totalPageCount == 2){
					%>
						<ul>
							<li class="current-page">&emsp;1&emsp;</li>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
					//ページ総数がNページの場合で、現在のページがNページ目
					else if(currentPage == totalPageCount && totalPageCount == 2){
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
						</ul>
					<%
					}
					//上記以外の場合
					else{
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
				}
				//ページ総数が3より大きい場合
				else if(totalPageCount >= 6){
					if(currentPage == 1){
					%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 1){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%> 
				<%
 				}else if(totalPageCount == 4){
 							if(currentPage == 1){
 				%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%>
				<%
				} else if(totalPageCount == 5){
							if(currentPage == 1){
				%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 4){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%>
				<%
				}
				%>
			</div>
			
			<!-- お気に入りで絞るモーダル -->
			<form action="${pageContext.request.contextPath}/TodoSearchServlet" method="POST">
				<section id="filtering-favoriteTask-modal">
					<h4>お気に入りタスクフィルタ</h4>
					<input type="submit" name="filteringFavoriteCheckClear" value="フィルタをクリア" onclick="submitFilteringFavoriteTask(value)">
					<input type="submit" name="filteringFavoriteCheck" value="フィルタをかける" onclick="submitFilteringFavoriteTask(value)">
					<input type="hidden" id="filteringFavorite" name="filteringFavorite" value="">
				</section>
			</form>
			<div id="filtering-favoriteTask-modal-mask"></div>
			
			
			<!-- 設定ボタン -->
			<div class="setting-button-container">
				<button id="setting-button" onclick="submitSetting()"><i class="fa-solid fa-gear"></i></button>
			</div>
			
			
			<!-- 設定ボタンを押下した際のコンテキストメニュー -->
			<div class="setting-context-menu">
				<ul id="context-menu" class="context-menu">
					<li id="menu-option-1"><a href="${pageContext.request.contextPath}/TodoCashServlet?menu-option=menu-option-1">ゴミ箱を開く</a></li>
					<li id="menu-option-2"><a href="${pageContext.request.contextPath}/TodoCashServlet?menu-option=menu-option-2">ゴミ箱内を削除する</a></li>
					<li id="menu-option-3"><a href="#" onclick="submitHiddenContextMenu()">閉じる</a></li>
				</ul>
			</div>
			
			<!-- 一覧表示 -->
			<table class="todo-list-table">
				<thead>
					<tr>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=id&any_pushedCounta=<%=any_pushedCounta%>">No<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("id")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("id")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=status&any_pushedCounta=<%=any_pushedCounta%>">ステータス<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("status")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("status")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=classification&any_pushedCounta=<%=any_pushedCounta%>">分類<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("classification")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("classification")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=task&any_pushedCounta=<%=any_pushedCounta%>">タスク名<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("task")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("task")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=description&any_pushedCounta=<%=any_pushedCounta%>">タスク概要<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("description")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("description")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=createDateTime&any_pushedCounta=<%=any_pushedCounta%>">作成日時<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("createDateTime")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("createDateTime")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=updateDateTime&any_pushedCounta=<%=any_pushedCounta%>">更新日時<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("updateDateTime")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("updateDateTime")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=creator&any_pushedCounta=<%=any_pushedCounta%>">作成者<%
						if(any_pushedCounta==1 && tHeaderParameter.equals("creator")){
						%><span class="sort-arrow">↑</span><%
						}else if(any_pushedCounta==0 && tHeaderParameter.equals("creator")){
						%><span class="sort-arrow">↓</span><%
						}
						%></a></th>
						<th>削除</th>		
						<th><a href="${pageContext.request.contextPath}/TodoOrderingServlet?tHeaderParameter=isFavorite&any_pushedCounta=<%=any_pushedCounta%>">お気に入り<%
								if(any_pushedCounta==1 && tHeaderParameter.equals("isFavorite")){
								%><span class="sort-arrow">↑</span><%
								}else if(any_pushedCounta==0 && tHeaderParameter.equals("isFavorite")){
								%><span class="sort-arrow">↓</span><%} %></a><span class="kebab-menu"><button id="filtering-favorite">︙</button></span></th>	
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
									<td><%= info.getCreateDateTime().format(DateTimeFormatter.ofPattern("y/M/d H:mm:ss")) %></td>
									<td><%= info.getUpdateDateTime().format(DateTimeFormatter.ofPattern("y/M/d H:mm:ss")) %></td>
									<td><%= info.getCreator() %></td>
									<td>
										<!-- 個別削除ボタン＝削除識別子は一意であるタスクNoと同値とする -->
										<label class="custom-checkbox">
											<form action="${pageContext.request.contextPath}/TodoDeleteServlet" class="delete-mark" method="POST">
												<input type="checkbox" name="individualDeleteId" value="<%= info.getId() %>" onchange="submitFormOnCheck(this)">
												<i class="far fa-trash-alt"></i>
											</form>
										</label>
									</td>
									<td>
										<!-- お気に入り登録機能 -->
										<form action="${pageContext.request.contextPath}/TodoFavoriteTaskRegisterServlet" class="favorite-mark" method="POST">
										<!-- あとで星マークに装飾 -->
											<input type="checkbox" class="favorite-checkbox" data-task-id="<%=info.getId() %>" name="favoriteTaskId" value="<%= info.getId() %>" onclick="submitFormOnCheckOfFavorite(this)">
											<input type="hidden" id="selectedFavIds" name="selectedFavIds" value="">
										</form>
									</td>
								</tr>
						<%
								}
						   }
						%>
						<%
							//取得したidをJSON形式で、JavaScriptに渡す
							if(request.getAttribute("favoriteTaskIdList") != null){
								StringBuilder jsonBuilder = new StringBuilder("[");
								List<Integer> favoriteTaskIdList = (List<Integer>)request.getAttribute("favoriteTaskIdList");
							    for (int i = 0; i < favoriteTaskIdList.size(); i++) {
							        jsonBuilder.append(favoriteTaskIdList.get(i));
							        if (i < favoriteTaskIdList.size() - 1) {
							            jsonBuilder.append(",");
							        }
							    }
							    jsonBuilder.append("]");
							    String favoriteTaskIdListJson = jsonBuilder.toString();
						 %>
								<div id="favoriteTaskData" data-tasks="<%= favoriteTaskIdListJson%>"></div>
						<%} %>
					</tbody>
				</thead>
			</table>
			

			<!-- ページネーション -->
			<div class="main-wrapper-pagination">
				<!-- ページ総数が3以下の場合 -->
				<%
				if(totalPageCount <= 3){
				%>
					<!-- ページ総数が1ページの場合＝常に1ページ目 -->
					<%
					if(totalPageCount == 1){
					%>
						<ul>
							<li class="current-page">&emsp;1&emsp;</li>
						</ul>
					<%
					}
					//ページ総数がNページの場合で、現在のページが1ページ目 -->
					else if(currentPage == 1 && totalPageCount == 2){
					%>
						<ul>
							<li class="current-page">&emsp;1&emsp;</li>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
					//ページ総数がNページの場合で、現在のページがNページ目
					else if(currentPage == totalPageCount && totalPageCount == 2){
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
						</ul>
					<%
					}
					//上記以外の場合
					else{
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
							<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
				}
				//ページ総数が3より大きい場合
				else if(totalPageCount >= 6){
					if(currentPage == 1){
					%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 1){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%> 
				<%
 				}else if(totalPageCount == 4){
 							if(currentPage == 1){
 				%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%>
				<%
				} else if(totalPageCount == 5){
							if(currentPage == 1){
				%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 4){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%>
				<%
				}
				%>
			</div>
		</main>
		<footer class="footer">
			<div class="wrapper">
				<p><small>&copy; 2024 ToDo-List</small></p>
			</div>
		</footer>
	</body>
</html>