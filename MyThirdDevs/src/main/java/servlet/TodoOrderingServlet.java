package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;

/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/TodoOrderingServlet")
public class TodoOrderingServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
	private static int any_pushedCounta = 0;
//	private static int id_pushedCounta = 0;
//	private static int status_pushedCounta = 0;
//	private static int classification_pushedCounta = 0;
//	private static int task_pushedCounta = 0;
//	private static int description_pushedCounta = 0;
//	private static int createDateTime_pushedCounta = 0;
//	private static int updateDateTime_pushedCounta = 0;
//	private static int creator_pushedCounta = 0;
//	private static int isFavorite_pushedCounta = 0;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
		forwardToTodoList(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tHeaderParameter = request.getParameter("tHeaderParameter");
		getOrderedHandleTask(request, response, tHeaderParameter);
	}
	
	protected static void getOrderedHandleTask(HttpServletRequest request, HttpServletResponse response, String tHeaderParameter) throws ServletException, IOException{
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//並べかえの処理を行わない場合
			if(tHeaderParameter == null || tHeaderParameter.isBlank()) {
				pagingHandleOfAllTask(request, response);
				return;
			}
			
			String tmpany_pushedCounta = request.getParameter("any_pushedCounta");
			if(tmpany_pushedCounta != null) {
				any_pushedCounta = Integer.valueOf(tmpany_pushedCounta);
			}

			//タスクを昇順か降順かを決定するために、使うカウンタ=以下全フィールドについて同様の処理
//			String tmpid_pushedCounta = request.getParameter("id_pushedCounta");
//			String tmpstatus_pushedCounta = request.getParameter("status_pushedCounta");
//			String tmpclassification_pushedCounta = request.getParameter("classification_pushedCounta");
//			String tmptask_pushedCounta = request.getParameter("task_pushedCounta");
//			String tmpdescription_pushedCounta = request.getParameter("description_pushedCounta");
//			String tmpcreateDateTime_pushedCounta = request.getParameter("createDateTime_pushedCounta");
//			String tmpupdateDateTime_pushedCounta = request.getParameter("updateDateTime_pushedCounta");
//			String tmpcreator_pushedCounta = request.getParameter("creator_pushedCounta");
//			String tmpisFavorite_pushedCounta = request.getParameter("isFavorite_pushedCounta");
//			
//			//idのnullチェックとintへの変換（以下、全フィールドに同様の処理)
//			if(tmpid_pushedCounta != null) {
//				id_pushedCounta = Integer.valueOf(tmpid_pushedCounta);
//			}
//			if(tmpstatus_pushedCounta != null) {
//				status_pushedCounta = Integer.valueOf(tmpstatus_pushedCounta);
//			}
//			if(tmpclassification_pushedCounta != null) {
//				classification_pushedCounta = Integer.valueOf(tmpclassification_pushedCounta);
//			}
//			if(tmptask_pushedCounta != null) {
//				task_pushedCounta = Integer.valueOf(tmptask_pushedCounta);
//			}
//			if(tmpdescription_pushedCounta != null) {
//				description_pushedCounta = Integer.valueOf(tmpdescription_pushedCounta);
//			}
//			if(tmpcreateDateTime_pushedCounta != null) {
//				createDateTime_pushedCounta = Integer.valueOf(tmpcreateDateTime_pushedCounta);
//			}
//			if(tmpupdateDateTime_pushedCounta != null) {
//				updateDateTime_pushedCounta = Integer.valueOf(tmpupdateDateTime_pushedCounta);
//			}
//			if(tmpcreator_pushedCounta != null) {
//				creator_pushedCounta = Integer.valueOf(tmpcreator_pushedCounta);
//			}
//			if(tmpisFavorite_pushedCounta != null) {
//				isFavorite_pushedCounta = Integer.valueOf(tmpisFavorite_pushedCounta);
//			}
			
			//各カウンタ、そのテーブルヘッダーをクライアント側に送信
//			request.setAttribute("id_pushedCounta", id_pushedCounta);
//			request.setAttribute("status_pushedCounta", status_pushedCounta);
//			request.setAttribute("classification_pushedCounta", classification_pushedCounta);
//			request.setAttribute("task_pushedCounta", task_pushedCounta);
//			request.setAttribute("description_pushedCounta", description_pushedCounta);
//			request.setAttribute("createDateTime_pushedCounta", createDateTime_pushedCounta);
//			request.setAttribute("updateDateTime_pushedCounta", updateDateTime_pushedCounta);
//			request.setAttribute("creator_pushedCounta", creator_pushedCounta);
//			request.setAttribute("isFavorite_pushedCounta", isFavorite_pushedCounta);
			request.setAttribute("any_pushedCounta", any_pushedCounta);
			request.setAttribute("tHeaderParameter", tHeaderParameter);
			
			//ページごとに表示するタスクを格納したリスト
			//TODO 2つ目の引数である数字をどう表現するか
			List<TodoInfo> orderedTaskList = EditDataDao.getListOrderByParameters(tHeaderParameter, any_pushedCounta);
			TodoSearchServlet.pagingHandleOnlyMatchTask(request, response, orderedTaskList);
			
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
