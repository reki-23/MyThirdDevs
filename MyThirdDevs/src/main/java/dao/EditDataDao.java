package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import common.ManageException;
import common.TodoInfo;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todoList VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todoList";
	private static final String getSql = "SELECT * FROM todoList";
	private static final String individualDeleteSql = "DELETE FROM todoList WHERE id = ?";
	
	
	//タスク取得
	public static List<TodoInfo> getRegisteredTask() throws ManageException{
		
		List<TodoInfo> todoList = new ArrayList<>();
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(getSql);
			ResultSet rs = ps.executeQuery()){
			
			while(rs.next()) {
				int id = rs.getInt(1);
				String status = rs.getString(2);
				String classification = rs.getString(3);
				String task = rs.getString(4);
				String description = rs.getString(5);
				LocalDateTime createDateTime = LocalDateTime.parse(rs.getString(6));
				LocalDateTime updateDateTime = LocalDateTime.parse(rs.getString(7));
				String creator = rs.getString(8);
				
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
			
			//確認用
			todoList.forEach(System.out::println);
			
			return todoList;
			
		}catch(SQLException e) {
			//あとでかく
			throw new ManageException("", e);
		}
	}
	
	
	//タスク個別登録、一括登録＝ここまだ未実装
	public static void registerNewTask(List<TodoInfo> newTaskList) throws ManageException{
		
		try(Connection con = dbc.getConnection();
			PreparedStatement ps = con.prepareStatement(insertSql)){
			
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
			ps.executeUpdate();
			
			System.out.println("通過確認");
			
		}catch(SQLException e) {
//			throw new ManageException("", e);
			e.printStackTrace();
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
