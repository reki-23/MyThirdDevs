package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.TodoInfo;
import dao.EditDataDao;
import exception.ManageException;
import model.DataValidator;
import model.WriteToFile;

/**
 * Servlet implementation class TodoExportServlet
 */
@WebServlet("/TodoExportServlet")
public class TodoExportServlet extends TodoServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
			//フィルターなしのときの値を取得
			String export_csv = request.getParameter("export-csv");
			//フィルタ―ありのときの値を取得
			String filter_submit = request.getParameter("filter_submit");
			//フィルタ―キャンセルの場合の値を取得
			String filter_cancel = request.getParameter("filter_cancel");
			
			//ファイルへの書き込みクラス
			WriteToFile writter = new WriteToFile();
			//ダウンロードしたいファイルへのパス
			String downloadsToPath = getServletContext().getRealPath("/downloads");
			Path downloadDir = Paths.get(downloadsToPath);
			if(!Files.exists(downloadDir)) {
				Files.createDirectories(downloadDir);
			}
			
			//フィルターをかけない場合
			if(export_csv != null) {
				exportCsvFile(request, response, writter, downloadDir);
			}
			
			//フィルターをかける場合
			if(filter_submit != null) {
				exportCsvFileWithFilter(request, response, writter, downloadDir);
			}
			
			//キャンセルの場合
			if(filter_cancel != null) {
				pagingHandleOfAllTask(request, response);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//フィルターなしでcsv出力する
	private void exportCsvFile(HttpServletRequest request, HttpServletResponse response, WriteToFile writter, Path downloadDir) throws ServletException, IOException{
		
		try {
			//現在登録されているタスクを取得
			List<TodoInfo> registeredTaskList = EditDataDao.getRegisteredTask();
			
			//ダウンロードしたいファイルへのパス
			String downloadFileToPath = downloadDir.resolve("task.csv").toString();
			
			//ファイルへの書き込み
			writter.writeToCsv(downloadFileToPath, registeredTaskList);
			
			//レスポンスヘッダーの設定
			File downloadedFile = new File(downloadFileToPath);
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "task.csv" + "\"");
			response.setContentLengthLong(downloadedFile.length());
			//ダウンロードするファイルを読み込み、出力ストリームを生成し一括書き込み後にコミット
			try(FileInputStream in = new FileInputStream(downloadedFile);
				ServletOutputStream out = response.getOutputStream()){
				in.transferTo(out);
				out.flush();
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
	
	
	//フィルタ―ありでcsv出力する
	private void exportCsvFileWithFilter(HttpServletRequest request, HttpServletResponse response, WriteToFile writter, Path downloadDir) throws ServletException, IOException {
		
		try {
			//フィルターの各値を取得
			String tmpId = request.getParameter("filter_id");
			String status = request.getParameter("status");
			String classification = request.getParameter("classification");
			String task = request.getParameter("task");
			String description = request.getParameter("description");
			String tmpCreateDateTime = request.getParameter("createDateTime");
			String tmpUpdateDateTime = request.getParameter("updateDateTime");
			String creator = request.getParameter("creator");
			
			//Stringからの型変換チェック
			int id = DataValidator.returnValidId(tmpId);
			LocalDateTime createDateTime = DataValidator.returnValidDateTime(tmpCreateDateTime);
			LocalDateTime updateDateTime = DataValidator.returnValidDateTime(tmpUpdateDateTime);
			
			//受取ったパラメータをリストに格納
			TodoInfo filteredTask = new TodoInfo.Builder().with(todo -> {
				todo.id = id;
				todo.status = status;
				todo.classification = classification;
				todo.task = task;
				todo.description = description;
				todo.createDateTime = createDateTime;
				todo.updateDateTime = updateDateTime;
				todo.creator = creator;
			}).build();
			
			//フィルター後のデータを取得
			List<TodoInfo> filteredTaskList = EditDataDao.getFilteredTaskList(filteredTask);

			//ダウンロードしたいファイルへのパス
			String downloadFileToPath = downloadDir.resolve("task_filter.csv").toString();
			
			//フィルター後のデータを書き込むクラスに渡す
			writter.writeToCsv(downloadFileToPath, filteredTaskList);
			
			//レスポンスヘッダーの設定
			File downloadedFile = new File(downloadFileToPath);
			response.setContentType("application/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "task_filter.csv" + "\"");
			response.setContentLengthLong(downloadedFile.length());
			//ダウンロードするファイルを読み込み、出力ストリームを生成し一括書き込み後にコミット
			try(FileInputStream in = new FileInputStream(downloadedFile);
				ServletOutputStream out = response.getOutputStream()){
				in.transferTo(out);
				out.flush();
			}
		}catch(ManageException e) {
			errorHandle(request, response, e);
		}
	}
}
