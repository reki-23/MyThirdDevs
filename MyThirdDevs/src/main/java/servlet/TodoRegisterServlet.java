package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;

/**
 * タスク登録用サーブレット
 */
@WebServlet("/TodoRegisterServlet")
public class TodoRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			//タスク登録で入力した情報を取得
			int id = Integer.parseInt(request.getParameter("id")); //例外処理必要
			String status = request.getParameter("status");
			String classification = request.getParameter("classification");
			String task = request.getParameter("task");
			String description = request.getParameter("description");
			String creator = request.getParameter("creator");
			
			
			//DB登録クラスに取得したパラメータを渡す
			List<TodoInfo> newTaskList = new ArrayList<TodoInfo>();
			newTaskList.add(new TodoInfo.Builder().with(todo -> {
				todo.id = id;
				todo.status = status;
				todo.classification = classification;
				todo.task = task;
				todo.description = description;
				todo.creator = creator;
			}).build());
			//登録処理
			EditDataDao.registerNewTask(newTaskList);
			
			
		}catch(Exception e) {
			
		};
		
	}
}
