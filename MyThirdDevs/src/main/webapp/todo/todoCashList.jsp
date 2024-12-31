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
		<link rel="stylesheet" href="/MyThirdDevs/todo/css/todoCashList.css" type="text/css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
		<script src="/MyThirdDevs/todo/js/utils.js"></script>
		<script src="/MyThirdDevs/todo/js/todoCashList/restore.js"></script>
		<script src="/MyThirdDevs/todo/js/todoCashList/modal.js"></script>
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
			
			<!-- 削除、復元に成功した場合 -->
			<%
			Boolean deleteJudge = (Boolean)request.getAttribute("deleteJudge");
			Boolean restoreJudge = (Boolean)request.getAttribute("restoreJudge");
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
	        if(restoreJudge != null && restoreJudge){%>
	        	<div class = "restore-success">
         			<h4>正常に復元されました。</h4>
					<br><h4 style="margin-top: 10px;">※復元後のタスクはタスク一覧の最後尾に保存されます。</h4>
        		</div>
	        <%
	        }else if(restoreJudge != null && !restoreJudge){%>
	        	<div class = "restore-failure">
	        		<h4>復元するデータがありません。</h4>
	        	</div>
	        <%}%>
	        
			
			<!-- タスク一覧へ戻る -->
			<a href="${pageContext.request.contextPath}/TopPageServlet" class="previous-todolist-btn">タスク一覧へ戻る</a>
			
			
			<!-- 復元ボタン -->
			<button id="restore-button">選択したタスクを復元する</button>
			
			
			<!-- 検索機能 -->
			<div class="search-task">
				<form action="${pageContext.request.contextPath}/TodoCashSearchServlet" method="POST">
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
							<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
					//ページ総数がNページの場合で、現在のページがNページ目
					else if(currentPage == totalPageCount && totalPageCount == 2){
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
						</ul>
					<%
					}
					//上記以外の場合
					else{
					%>
						<ul>
							<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
							<li class="current-page">&emsp;<%=currentPage%>&emsp;</li>
							<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						</ul>
					<%
					}
				}
				//ページ総数が6より大きい場合
				else if(totalPageCount >= 6){
					if(currentPage == 1){
					%>
					<ul>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 1){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount - 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
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
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 3){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
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
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == totalPageCount){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
					</ul>
					<%
					}else if(currentPage == 4){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
					</ul>
					<%
					} else if(currentPage == 2){
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+2%>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;...&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					} else{
					%>
					<ul>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=1&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;1&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage-1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage-1%>&emsp;</a></li>
						<li class="current-page"><%=currentPage%></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=currentPage+1 %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=currentPage+1%>&emsp;</a></li>
						<li><a href="${pageContext.request.contextPath}/TodoCashSearchServlet?pageNum=<%=totalPageCount %>&searchWord=<%=searchWord %>&tHeaderParameter=<%=tHeaderParameter%>">&emsp;<%=totalPageCount%>&emsp;</a></li>
					</ul>
					<%
					}
					%>
				<%
				}
				%>
			</div>
			
			
			<!-- 復元時モーダル -->
			<form action="${pageContext.request.contextPath}/TodoCashDeleteRestoreServlet?menu-option=menu-option-3" method="POST">
				<section id="restore_confirm_modal">
					<h3>復元しますか？</h3>
					<input type="submit" name="restore_submit" value="決定">
					<input type="submit" name="restore_cancel" value="キャンセル" onclick="closeRestoreConfirmModal()">
					<input type="hidden" id="selectedRestoreIds" name="selectedRestoreIds" value="">
				</section>			
			</form>
			<div id="res-mask"></div>
			
			
			<!-- 設定ボタン -->
			<div class="setting-button-container">
				<button id="setting-button" onclick="submitSetting()"><i class="fa-solid fa-gear"></i></button>
			</div>
			
			
			<!-- 設定ボタンを押下した際のコンテキストメニュー -->
			<div class="setting-context-menu">
				<ul id="context-menu" class="context-menu">
					<li id="menu-option-1"><a href="${pageContext.request.contextPath}/TodoCashDeleteRestoreServlet?menu-option=menu-option-1">全てのデータを復元する</a></li>
					<li id="menu-option-2"><a href="${pageContext.request.contextPath}/TodoCashDeleteRestoreServlet?menu-option=menu-option-2">全てのデータを削除する</a></li>
					<li id="menu-option-3"><a href="#" onclick="submitHiddenContextMenu()">閉じる</a></li>
				</ul>
			</div>
			
			
			<!-- 一覧表示 -->
			<table class="todo-list-table">
				<thead>
					<tr>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=id&any_pushedCounta=<%=any_pushedCounta%>">No<%if(any_pushedCounta==1 && tHeaderParameter.equals("id")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("id")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=status&any_pushedCounta=<%=any_pushedCounta%>">ステータス<%if(any_pushedCounta==1 && tHeaderParameter.equals("status")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("status")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=classification&any_pushedCounta=<%=any_pushedCounta%>">分類<%if(any_pushedCounta==1 && tHeaderParameter.equals("classification")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("classification")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=task&any_pushedCounta=<%=any_pushedCounta%>">タスク名<%if(any_pushedCounta==1 && tHeaderParameter.equals("task")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("task")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=description&any_pushedCounta=<%=any_pushedCounta%>">タスク概要<%if(any_pushedCounta==1 && tHeaderParameter.equals("description")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("description")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=createDateTime&any_pushedCounta=<%=any_pushedCounta%>">作成日時<%if(any_pushedCounta==1 && tHeaderParameter.equals("createDateTime")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("createDateTime")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=updateDateTime&any_pushedCounta=<%=any_pushedCounta%>">更新日時<%if(any_pushedCounta==1 && tHeaderParameter.equals("updateDateTime")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("updateDateTime")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th><a href="${pageContext.request.contextPath}/TodoCashOrderingServlet?tHeaderParameter=creator&any_pushedCounta=<%=any_pushedCounta%>">作成者<%if(any_pushedCounta==1 && tHeaderParameter.equals("creator")){%><span class="sort-arrow">↑</span><%}else if(any_pushedCounta==0 && tHeaderParameter.equals("creator")){%><span class="sort-arrow">↓</span><%} %></a></th>
						<th>復元</th>
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
									<td>
										<!-- 個別復元ボタン＝復元識別子は一意であるタスクNoと同値とする -->
										<form action="${pageContext.request.contextPath}/TodoCashDeleteRestoreServlet" class="restore-mark" method="POST">
											<input type="checkbox" name="individualRestoreId" value="<%= info.getId() %>" onchange="submitFormOnCheck(this)">
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