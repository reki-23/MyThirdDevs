package servlet.todoMain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;
import model.DataValidator;
import model.FileAnomalyCheck;
import model.ReadFile;

/**
 * タスク登録用サーブレット
 */
@WebServlet("/TodoRegisterServlet")
@MultipartConfig
public class TodoRegisterServlet extends TodoServlet{
	private static final long serialVersionUID = 1L;
   
	//プロパティファイルから読みこむメッセージ
	private static String registerMessage;
	//一括登録かを判定するフラグ
	private static boolean isBulkJudge;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pagingHandleOfAllTask(request, response);
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
			
			//一括登録
			if(bulkRegister != null) {
				bulkRegisterTask(request, response);
				forwardToTodoList(request, response);
			}
			
			//個別登録
			else if(registerSubmit != null) {
				individualRegisterTask(request, response);	
				forwardToTodoList(request, response);
			
			//キャンセル
			}else if(registerCancel != null) {
				pagingHandleOfAllTask(request, response);
				forwardToTodoList(request, response);
			}
			
		}catch(Exception e) {
			
		}
	}
	
	//一括登録
	private void bulkRegisterTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//一括であることを表す
		isBulkJudge = true;
		//ファイルの実体を取得
		Part filePart = request.getPart("csvFile");
		//ファイル名を取得
		String filePartName = filePart.getSubmittedFileName();
		//ファイルがあるフォルダへのパスを取得
		String uploadDir = getServletContext().getRealPath("/bulkUploads");
		Path uploadDirPath = Paths.get(uploadDir);
		if(!Files.exists(uploadDirPath)) {
			Files.createDirectory(uploadDirPath);
		}
		
		//ファイルまでのパス
		Path filePath = Paths.get(uploadDir, filePartName);
		String fileName = filePath.toString();
		
		//ファイルの保存
		filePart.write(fileName);

		try {
			//異常検査
			FileAnomalyCheck fac = new FileAnomalyCheck();
			fac.anomalyCheck(filePartName, filePath);
			
			//プロパティファイル読み込み
			Properties prop = new Properties();
			getPropertiesFileInfo(request, response, prop);
			
			//ファイルを読み込む
			ReadFile readFile = new ReadFile();
			List<TodoInfo> readTodoList = readFile.readCsvFile(fileName);
			
			//読みこんだ結果をDBに登録
			int bulkRegisteredCount = EditDataDao.registerNewTask(readTodoList, isBulkJudge);
			
			//登録した件数をプロパティファイルから読み込んだメッセージとして出力できるようにする
			registerMessage = MessageFormat.format(prop.getProperty("bulkRegister.submit"), bulkRegisteredCount);
			request.setAttribute("registerMessage", registerMessage);
			pagingHandleOfAllTask(request, response);
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}		
	}
	
	//個別登録
	private void individualRegisterTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//プロパティファイル読み込み
		Properties prop = new Properties();
		getPropertiesFileInfo(request, response, prop);
		
		//タスク登録時のエラーメッセージ
		List<String> errorMessageList = new ArrayList<>();
		
		//タスク登録で入力した情報を取得
		String strId = request.getParameter("id");
		int tempId = 0;
		if(strId == null || strId.isBlank()) {
			errorMessageList.add(prop.getProperty("id.error"));
		}else {
			try {
				tempId = DataValidator.returnValidId(strId);				
			}catch(ManageException e) {
				errorHandle(request, response, e);
			}
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
			//一覧表示
			pagingHandleOfAllTask(request, response);
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
			try {
				//登録処理
				EditDataDao.registerNewTask(newTaskList, isBulkJudge);
				registerMessage = prop.getProperty("register.submit");
				request.setAttribute("registerMessage", registerMessage);
				pagingHandleOfAllTask(request, response);
			}catch(ManageException e) {
				errorHandle(request, response, e);
			}			
		}
	}
}
