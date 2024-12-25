package servlet.todomain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.common.TodoInfo;
import model.exception.ManageException;
import model.todomain.WriteToFile;
import model.validation.DataValidator;
import servlet.TodoServlet;

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
			
			//ファイルへの書き込みクラスのインスタンス生成
			WriteToFile writter = new WriteToFile();
			
			//ダウンロードしたいファイルへのパス
			String downloadsToPath = getServletContext().getRealPath("/downloads");
			Path downloadDir = Paths.get(downloadsToPath);
			if(!Files.exists(downloadDir)) {
				Files.createDirectories(downloadDir);
			}
			
			//フィルターをかけない場合
			if(export_csv != null) {
				//ダウンロードするファイルまでのパス
				String downloadFileToPath = writter.exportCsvFile(writter, downloadDir);
				//ダウンロード処理
				downloadHandling(request, response, downloadFileToPath, filter_submit);
			}
			
			//フィルターをかける場合
			if(filter_submit != null) {
				//フィルターの各値を取得
				String tmpId = request.getParameter("filter_id");
				String status = request.getParameter("status");
				String classification = request.getParameter("classification");
				String task = request.getParameter("task");
				String description = request.getParameter("description");
				String tmpCreateDateTime = request.getParameter("createDateTime");
				String tmpUpdateDateTime = request.getParameter("updateDateTime");
				String creator = request.getParameter("creator");
				String tmpIsFavorite = request.getParameter("isFavorite");
			
				//Stringからの型変換チェック
				int id = DataValidator.returnValidId(tmpId);
				LocalDateTime createDateTime = DataValidator.returnValidDateTime(tmpCreateDateTime);
				LocalDateTime updateDateTime = DataValidator.returnValidDateTime(tmpUpdateDateTime);
				boolean isFavorite = DataValidator.returnValidFavoriteFlg(tmpIsFavorite);
				
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
					todo.isFavorite = isFavorite;
				}).build();
				
				//フィルターをかけてCSVファイルを出力
				String downloadFileToPath =  writter.exportCsvFileWithFilter(writter, downloadDir, filteredTask);
				//ダウンロード処理
				downloadHandling(request, response, downloadFileToPath, filter_submit);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	//ダウンロード処理
	private void downloadHandling(HttpServletRequest request, HttpServletResponse response, String downloadFileToPath, String filter_submit) throws ServletException, IOException, ManageException{
		//出力するCSVファイルの名前
		String csvFileName = null;
		if(filter_submit != null) {
			csvFileName = "task_filter.csv";
		}else{
			csvFileName = "task.csv";
		}
		//レスポンスヘッダーの設定
		File downloadedFile = new File(downloadFileToPath);
		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + csvFileName + "\"");
		response.setContentLengthLong(downloadedFile.length());
		//ダウンロードするファイルを読み込み、出力ストリームを生成し一括書き込み後にコミット
		try(FileInputStream in = new FileInputStream(downloadedFile);
				ServletOutputStream out = response.getOutputStream()){
			in.transferTo(out);
			out.flush();
		}
	}
}
