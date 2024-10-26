package dao;

public class EditDataDao {
	
	private static final DBConnection dbc = new DBConnection();
	private static final String insertSql = "INSERT INTO todoList VALUES(?,?,?,?,?,?,?,?)";
	private static final String deleteSql = "DELETE FROM todoList";
	private static final String getSql = "SELECT * FROM todoList";
	
	
}
