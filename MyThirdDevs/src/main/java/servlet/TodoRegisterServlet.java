package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import common.ManageException;
import common.PropertiesFileNotFoundException;
import common.TodoInfo;
import dao.EditDataDao;

/**
 * タスク登録用サーブレット
 */
@WebServlet("/TodoRegisterServlet")
@MultipartConfig
public class TodoRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	//プロパティファイルから読みこむメッセージ
	private static String registerMessage;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {	
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding("UTF-8");

			//登録するボタン押下で送信される値を取得
			String registerSubmit = request.getParameter("register_submit");
			//キャンセルボタン押下で送信される値を取得
			String registerCancel = request.getParameter("register_cancel");
			//一括登録ボタン押下で送信される値を取得
			String bulkRegister = request.getParameter("bulk_register");
			
			Properties prop = new Properties();
			try(InputStream input = getClass().getResourceAsStream("/logging.properties")){
				if(input == null) {
					//読み込み失敗時
					throw new ManageException("EM001", new PropertiesFileNotFoundException());
				}
				
				try(InputStreamReader reader = new InputStreamReader(input, "UTF-8")){
					prop.load(reader);
				}
			}
			
			//一括登録
			if(bulkRegister != null) {
				bulkRegisterTask(request, response);
			}
			
			//個別登録
			else if(registerSubmit != null) {
				individualRegisterTask(request, response, prop);	
			
			//キャンセル
			}else if(registerCancel != null) {
				displayRegisteredTask(request, response);
			}
			
		}catch(Exception e) {
			
		}
	}
	
	//タスク一覧取得
	private static void displayRegisteredTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ManageException{
		List<TodoInfo> todoList = EditDataDao.getRegisteredTask();
		request.setAttribute("todoList", todoList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todoList.jsp");
		dispatcher.forward(request, response);
	}
	
	//一括登録
	private static void bulkRegisterTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ManageException{
		System.out.println("一括登録");
		
		//ファイルの実体を取得
		Part filePart = request.getPart("csvFile");
		//ファイル名を取得
		String fileName = filePart.getSubmittedFileName();
		
		//ファイル名をファイル内容処理用のメソッドの引数に渡して、処理する
		
	}
	
	//個別登録
	private static void individualRegisterTask(HttpServletRequest request, HttpServletResponse response, Properties prop) throws ServletException, IOException, ManageException{
		System.out.println("個別登録");
		
		//タスク登録時のエラーメッセージ
		List<String> errorMessageList = new ArrayList<>();
		
		//タスク登録で入力した情報を取得
		String strId = request.getParameter("id");
		int tempId = 0;
		if(strId == null || strId.isBlank()) {
			errorMessageList.add(prop.getProperty("id.error"));
		}else {
			tempId = Integer.parseInt(strId); //ここ例外処理必要
		}
		int id = tempId;
		String status = request.getParameter("status");
		String classification = request.getParameter("classification");
		String task = request.getParameter("task");
		String description = request.getParameter("description");
		String creator = request.getParameter("creator");
		
		//各パラメータの空チェック
		if(status == null || status.isBlank()) {
			errorMessageList.add(prop.getProperty("status.error"));
		}
		if(classification == null || classification.isBlank()) {
			errorMessageList.add(prop.getProperty("classification.error"));
		}
		if(task == null || task.isBlank()) {
			errorMessageList.add(prop.getProperty("task.error"));
		}
		if(description == null || description.isBlank()) {
			errorMessageList.add(prop.getProperty("description.error"));
		}
		if(creator == null || creator.isBlank()) {
			errorMessageList.add(prop.getProperty("creator.error"));
		}
		
		//エラーの場合、エラーメッセージとともにtodoList.jspへ
		if(errorMessageList.size() > 0) {
			request.setAttribute("errorMessageList", errorMessageList);
		}else {
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
			//登録処理・登録完了メッセージ
			EditDataDao.registerNewTask(newTaskList);
			registerMessage = prop.getProperty("register.submit");
			request.setAttribute("registerMessage", registerMessage);
			
		}
		//一覧表示
		displayRegisteredTask(request, response);
	}
	
}
