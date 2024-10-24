package model;

import java.time.LocalDateTime;

/*
 * 
 * あとからフィールドを追加された時の保守性を考えて、デザインパターンを導入
 * Builder？？
 * 
 */

public class TodoInfo {
	
	private int id;
	private String status;
	private String classification;
	private String task;
	private String description;
	private LocalDateTime createDateTime;
	private LocalDateTime updateDateTime;
	private String creator;	
}
