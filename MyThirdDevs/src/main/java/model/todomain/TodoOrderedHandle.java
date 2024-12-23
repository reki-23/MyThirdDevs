//package model.todomain;
//
///*
// * 
// * 取得したデータを並べかえするクラス
// * 
// */
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import model.common.TodoInfo;
//import model.dao.EditDataDao;
//import model.exception.ManageException;
//import servlet.todomain.TodoSearchServlet;
//
//public class TodoOrderedHandle {
//	protected void getOrderedHandleTask(String tHeaderParameter) throws ServletException, IOException{
//		//ページごとに表示するタスクを格納したリスト
//		//TODO 2つ目の引数である数字をどう表現するか
//		List<TodoInfo> orderedTaskList = EditDataDao.getListOrderByParameters(tHeaderParameter, any_pushedCounta);
//		
//	}
//}
