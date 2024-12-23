package model.todomain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import model.common.TodoInfo;
import model.common.TodoPagingInfo;
import model.dao.EditDataDao;
import model.exception.ManageException;

/*
 * ページング処理を行うクラス
 */

public class TodopagingProcess {
	
	//全件データを取得する際のページング処理
	public TodoPagingInfo<TodoInfo> pagingHandleOfAllTask(String tmpPageNum) throws ManageException{
		//ページごとに表示するタスクを格納したリスト
		List<TodoInfo> pageByPageTaskList = new ArrayList<>();
		//1ページに表示するタスク数
		final int solidTaskCount = 50;
		//デフォルトのページ番号
		int pageNum = 1;
		//現在のページ番号を取得
		if(tmpPageNum != null){
			pageNum = Integer.parseInt(tmpPageNum); //あとで例外処理を追加
		}
		
		//現在のページ・タスク総数・必要なページ数を初期化
		int currentPage = 1;
		int totalTaskCount = 0;
		int totalPageCount = 1;
		
		//タスク件数・ページ数計算
		totalTaskCount = EditDataDao.getRegisteredTask().size();
		currentPage = pageNum;
		totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
		
		//ページごとのデータを格納したリスト
		pageByPageTaskList = EditDataDao.getSpecifyColumnTask(pageNum, solidTaskCount);
		
		return new TodoPagingInfo<>(pageByPageTaskList, totalPageCount, currentPage);
	}
	
	
	//特定の条件で絞られたデータを取得した際のページング処理
	public TodoPagingInfo<TodoInfo> pagingHandleOfSpecificTask(String tmpPageNum, List<TodoInfo> resultTask) throws ServletException, IOException{
		
		//ページごとに表示するタスクを格納したリスト
		List<TodoInfo> pageByPageTaskList = new ArrayList<>();
		//1ページに表示するタスク数
		final int solidTaskCount = 50;
		//デフォルトのページ番号
		int pageNum = 1;
		//現在のページ番号を取得				
		if(tmpPageNum != null){
			pageNum = Integer.parseInt(tmpPageNum); //あとで例外処理を追加
		}
		
		//現在のページ・タスク総数・必要なページ数を初期化
		int currentPage = 1;
		int totalTaskCount = 0;
		int totalPageCount = 1;
		
		//タスク件数・ページ数計算
		currentPage = pageNum;
		totalTaskCount = resultTask.size();
		totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
		
		//そのページに表示する最初の行番号を取得
		int start = (pageNum - 1) * solidTaskCount;
		//そのページに表示する最後の行番号を取得
		//この2つのうち最小のほうがそのページに表示するリストの数となる
		//第一引数はそのページの要素数に限らず、+51ずつの値となるが、第二引数はそのページに表示するデータの要素数を表す
		//例えば、120件取得した場合、第一引数は151、第二引数は120を取得する
		int end = Math.min(pageNum * solidTaskCount - 1, resultTask.size());
		
		//検索結果が保存されているリストを取得
		pageByPageTaskList = resultTask.subList(start, end);
		
		return new TodoPagingInfo<TodoInfo>(pageByPageTaskList, totalPageCount, currentPage);
	}
	
	
//	ゴミ箱テーブル内のデータを取得しページング処理を行うクラス
	public TodoPagingInfo<TodoInfo> pagingHandleOfAllCashTask(String tmpPageNum) throws ManageException{
		
		//ページごとに表示するタスクを格納したリスト
		List<TodoInfo> pageByPageTaskList = new ArrayList<>();
		//1ページに表示するタスク数
		final int solidTaskCount = 50;
		//デフォルトのページ番号
		int pageNum = 1;
		//現在のページ番号を取得			
		if(tmpPageNum != null){
			pageNum = Integer.parseInt(tmpPageNum); //あとで例外処理を追加
		}
		
		//現在のページ・タスク総数・必要なページ数を初期化
		int currentPage = 1;
		int totalTaskCount = 0;
		int totalPageCount = 1;
		
		//タスク件数・ページ数計算
		currentPage = pageNum;
		totalTaskCount = EditDataDao.getCashedTask().size();
		totalPageCount = (totalTaskCount - 1) / solidTaskCount + 1;
		
		pageByPageTaskList = EditDataDao.getSpecifyColumnCashTask(pageNum, solidTaskCount);
			
		return new TodoPagingInfo<>(pageByPageTaskList, totalPageCount, currentPage);
	}
}
