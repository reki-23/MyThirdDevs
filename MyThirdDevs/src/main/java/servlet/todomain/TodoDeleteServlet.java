package servlet.todomain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.EditDataDao;
import model.exception.ManageException;
import servlet.TodoServlet;

/**
 * タスク削除用サーブレット
 */
@WebServlet("/TodoDeleteServlet")
public class TodoDeleteServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//削除タイプを取得
			String deleteType = request.getParameter("deleteType");
			//選択されたタスクidを取得=個別削除時
			String[] selectedIds = request.getParameterValues("selectedIds");
			//削除確認モーダルの値を取得
			String delSubmit = request.getParameter("delSubmit");
			
			//削除確認モーダルで決定するを押下した場合
			if(delSubmit != null) {
				//一括削除処理
				if(deleteType.equals("bulk")) {
					boolean bulkDeleteJudge = EditDataDao.bulkDeleteTask();
					request.setAttribute("deleteJudge", bulkDeleteJudge);
				}
				
				//個別削除処理
				if(deleteType.equals("individual")) {
					List<Integer> deletedIdList = Arrays
							.stream(selectedIds)
							.flatMap(id -> Arrays.stream(id.split(",")))
							.map(Integer::valueOf)
							.collect(Collectors.toList());
					boolean individualDeleteJudge = EditDataDao.individualDeleteTask(deletedIdList);
					request.setAttribute("deleteJudge", individualDeleteJudge);
				}				
			}
			pagingHandleOfAllTask(request, response);
			forwardToTodoList(request, response);
			
		}catch(ManageException e) {
			errorHandle(request, response, e);		
		}
	}
}
