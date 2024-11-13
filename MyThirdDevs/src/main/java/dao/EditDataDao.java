package dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.TodoInfo;
import exception.ManageException;
import exception.NoElementsOnFileException;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todolist VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todolist";
	private static final String getSql = "SELECT * FROM todolist";
	private static final String individualDeleteSql = "DELETE FROM todolist WHERE id = ?";
	private static final String invalidId = "-1";
	private static final String invalidCreateDateTime = LocalDateTime.MIN.toString();
	private static final String invalidUpdateDateTime = LocalDateTime.MIN.toString();
//	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	
	//タスク取得
	public static List<TodoInfo> getRegisteredTask() throws ManageException{
		
		List<TodoInfo> todoList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(getSql);
			ResultSet rs = ps.executeQuery()){
			
			//データ取得
			while(rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String classification = rs.getString("classification");
				String task = rs.getString("task");
				String description = rs.getString("description");
				LocalDateTime createDateTime = rs.getTimestamp("createDateTime").toLocalDateTime();
				LocalDateTime updateDateTime = rs.getTimestamp("updateDateTime").toLocalDateTime();
				String creator = rs.getString("creator");
				
				//データを追加
				todoList.add(new TodoInfo.Builder().with(todo -> {
					todo.id = id;
					todo.status = status;
					todo.classification = classification;
					todo.task = task;
					todo.description = description;
					todo.createDateTime = createDateTime;
					todo.updateDateTime = updateDateTime;
					todo.creator = creator;
				}).build());
			}
			return todoList;
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク個別登録、一括登録
	public static int registerNewTask(List<TodoInfo> newTaskList, boolean isBulkJudge) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(insertSql)){
			
			if(!isBulkJudge) {
				//個別登録
				TodoInfo newTask = newTaskList.get(0);
				ps.setInt(1, newTask.getId());
				ps.setString(2, newTask.getStatus());
				ps.setString(3, newTask.getClassification());
				ps.setString(4, newTask.getTask());
				ps.setString(5, newTask.getDescription());
				ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
				//これは更新日時なので、登録時点ではなく、タスク詳細画面でタスクを編集し、決定した時間を取得するようにする。
				//今は仮で、今の時間を取得する
				ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
				ps.setString(8, newTask.getCreator());
				int registeredCount = ps.executeUpdate();
				return registeredCount;
			}else {
				//一括登録
				for(TodoInfo newTask : newTaskList) {
					ps.setInt(1, newTask.getId());
					ps.setString(2, newTask.getStatus());
					ps.setString(3, newTask.getClassification());
					ps.setString(4, newTask.getTask());
					ps.setString(5, newTask.getDescription());
					ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
					ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); //ここは更新日時に変更する
					ps.setString(8, newTask.getCreator());
					ps.addBatch();
				}
				//一括実行（登録）
				int[] bulkRegisteredCount =  ps.executeBatch();
				return bulkRegisteredCount.length;
			}
		
		//整合性制約違反
		}catch(SQLIntegrityConstraintViolationException e) {
			throw new ManageException("EM009", e);
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク一括削除
	public static boolean bulkDeleteTask() throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(deleteSql)){
			//削除件数
			int deleteCount = ps.executeUpdate();
			if(deleteCount > 0) {
				//trueなら削除成功
				return true;
			}else {
				//falseなら削除失敗
				return false;				
			}
			
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	//タスク個別削除
	public static boolean individualDeleteTask(List<Integer> deletedIdList) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(individualDeleteSql)){
				
			//押下されたタスクのIDを受取り削除する処理
			for(Integer deletedId : deletedIdList) {
				ps.setInt(1, deletedId);
				ps.addBatch();
			}
			//一括削除
			int[] indiDelCount = ps.executeBatch();
			if(indiDelCount.length > 0) {
				return true;
			}else {
				return false;
			}		
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("EM003", e);
		}
	}
	
	
	//フィルターをかけて一覧取得
	public static List<TodoInfo> getFilteredTaskList(TodoInfo filteredTask, List<String> paramsList) throws ManageException{
		
		//フィルター後のデータを格納するリスト
		List<TodoInfo> filteredTaskList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(getSql);
			ResultSet rs = ps.executeQuery();){
			
			boolean isMatchTask = false;
			
			//送られてきた情報をリストに格納
			List<TodoInfo> infoList = new ArrayList<>();
			infoList.add(new TodoInfo.Builder().with(info -> {
				info.id = filteredTask.getId();
				info.status = filteredTask.getStatus();
				info.classification = filteredTask.getClassification();
				info.task = filteredTask.getTask();
				info.description = filteredTask.getDescription();
				info.createDateTime = filteredTask.getCreateDateTime();
				info.updateDateTime = filteredTask.getUpdateDateTime();
				info.creator = filteredTask.getCreator();
			}).build());
			
			//infoListの中で空ではない要素をMapとして保存する
			Map<Integer, String> infoMap = new HashMap<>();
			for(int i = 0; i < paramsList.size(); i++) {
				if(!paramsList.get(i).isBlank()) {
					infoMap.put(i, paramsList.get(i));					
				}
			}
//			infoMap.entrySet().stream().forEach(map -> System.out.println(map.getKey() + ", " + map.getValue()));
			
			//Mapに保存された値を”かつ条件”として連結した文字列を返す
			String andConditions = createConditionalExp(infoMap);
			//文字列をifの条件式に変換する
			String equals = "equals";
			
			// TODO　以下ちゃんと直す必要あり
			/*
			Class<?> clazz = andConditions.getClass();
			try {
				Object obj = clazz.getMethod(equals, Object.class).invoke(andConditions);
				System.out.println(obj);
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			*/
			
			
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String status = rs.getString("status");
				String classification = rs.getString("classification");
				String task = rs.getString("task");
				String description = rs.getString("description");
				LocalDateTime createDateTime = rs.getTimestamp("createDateTime").toLocalDateTime();
				LocalDateTime updateDateTime = rs.getTimestamp("updateDateTime").toLocalDateTime();
				String creator = rs.getString("creator");

				
				//要素が1つの場合、”または”条件で取りだす
				if(paramsList.size() == 1) {
					isMatchTask = filteredTask.getId() == id || filteredTask.getStatus().equals(status) || filteredTask.getClassification().equals(classification) ||
								  filteredTask.getTask().equals(task) || filteredTask.getDescription().equals(description) || filteredTask.getCreateDateTime().equals(createDateTime) ||
							      filteredTask.getUpdateDateTime().equals(updateDateTime) || filteredTask.getCreator().equals(creator);					
				}
				
				//要素が複数ある場合、”かつ”条件で取りだす
				else if(paramsList.size() > 1) {
					
					
				}
				
				if(isMatchTask) {
					filteredTaskList.add(new TodoInfo.Builder().with(todo -> {
						todo.id = id;
						todo.status = status;
						todo.classification = classification;
						todo.task = task;
						todo.description = description;
						todo.createDateTime = createDateTime;
						todo.updateDateTime = updateDateTime;
						todo.creator = creator;
					}).build());
				}
			}
			
			//1つも条件に一致しなかった場合
			if(filteredTaskList.size() == 0) {
				throw new ManageException("EM016", new NoElementsOnFileException());
			}
			
			return filteredTaskList;
			
			
		}catch(SQLException e) {
			throw new ManageException("EM003", e);
		}
	}
	
	
	private static String createConditionalExp(Map<Integer, String> infoMap) {
		int creatingCount = infoMap.size();
		StringBuilder builder = new StringBuilder();
		//infoMapの要素番号を管理
		int index = 0;
		for(Map.Entry<Integer, String> entry : infoMap.entrySet()) {
			//infoMapの最後の要素には"&&"を連結しない
			if(index == creatingCount - 1) {
				builder.append(entry.getValue());
			}else {				
				index++;
				builder.append(entry.getValue()).append("&&");
			}
		}
		return builder.toString();
	}
}
