package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import common.TodoInfo;
import exception.ManageException;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todolist VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todolist";
	private static final String getSql = "SELECT * FROM todolist";
	private static final String individualDeleteSql = "DELETE FROM todolist WHERE id = ?";
	
	
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
			throw new ManageException("", e);
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
			
		}catch(SQLException e) {
			throw new ManageException("", e);
			
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
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
	
	//タスク個別削除
	public static boolean individualDeleteTask(List<Integer> deletedIdList) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(individualDeleteSql)){
				
			/*
			 * 押下されたタスクのIDを受取り削除する処理
			 */
			//複数選択、1つ選択の場合、削除
			for(Integer deletedId : deletedIdList) {
				ps.setInt(1, deletedId);
				ps.addBatch();
			}
			//一括削除
			ps.executeBatch();
			if(ps.executeBatch().length > 0) {
				return true;
			}else {
				return false;
			}	
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
}
