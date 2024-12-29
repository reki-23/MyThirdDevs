package model.dao;


/*
 * DAOクラスで使用するクエリを一元管理するクラス
 */
public class TaskQueries {
	
	private TaskQueries() {
		//外部からインスタンス化できないようにする
	}
	static final String insertSql = "INSERT INTO todolist VALUES(?,?,?,?,?,?,?,?,?)";
	static final String insertCashSql = "INSERT INTO todocashlist (id,status,classification,task,description,createDateTime,updateDateTime,creator,isFavorite) SELECT * FROM todolist WHERE id = ? ORDER BY id ASC";
	static final String insertAllCashSql = "INSERT INTO todocashlist (id,status,classification,task,description,createDateTime,updateDateTime,creator,isFavorite) SELECT * FROM todolist ORDER BY id ASC";
	static final String selectIdSql = "SELECT * FROM todolist WHERE id = ?";
	static final String selectFavIdSql = "SELECT isFavorite FROM todolist WHERE id = ?";
	static final String selectFavSql = "SELECT * FROM todolist WHERE isFavorite = 1";
	static final String deleteSql = "DELETE FROM todolist";
	static final String deleteCashSql = "TRUNCATE TABLE todocashlist";
	static final String getSql = "SELECT * FROM todolist";
	static final String getCashSql = "SELECT * FROM todocashlist ORDER BY id ASC";
	static final String updateFavFlgSql = "UPDATE todolist SET isFavorite = ? WHERE id = ?";
	static final String individualDeleteSql = "DELETE FROM todolist WHERE id = ?";
	static final String getLimitOffsetQuery = "SELECT * FROM todolist LIMIT 50 OFFSET ?";
	static final String getCashLimitOffsetQuery = "SELECT * FROM todocashlist ORDER BY id ASC LIMIT 50 OFFSET ?";
	static String dynamicQuery = "SELECT * FROM todolist WHERE 1=1";
	static String dynamicCashedQuery = "SELECT * FROM todocashlist WHERE 1=1";
	
	
	static final String restoreToQuery = 
		    "INSERT INTO todolist (id, status, classification, task, description, createDateTime, updateDateTime, creator, isFavorite) " +
		    "SELECT " +
		    "  (SELECT COALESCE(MAX(id), 0) FROM todolist) + ROW_NUMBER() OVER (ORDER BY todocashlist.id) AS id, " +
		    "  status, classification, task, description, createDateTime, updateDateTime, creator, isFavorite " +
		    "FROM todocashlist";
	
}
