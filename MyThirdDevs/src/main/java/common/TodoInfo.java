package common;

import java.time.LocalDateTime;
import java.util.function.Consumer;

/*
 * 
 * Builderパターン導入
 * →Consumerインタフェースはインスタンスを生成するBuilderに利用できる
 * →Builderはコンストラクタの代わりにsetterを使って初期化するのに必要な値を受け取る
 * →setterでの初期化値を呼びだす場合、フィールド数が変更になってもコンストラクタに影響が出ない
 * →しかし、setterが多くなると保守性が下がる
 * →Consumerを使うと解決する
 * →Builderのフィールドをpublicとして宣言する。Consumer<Builder>を受け取るwith()を定義する
 * →with()はBuilder型の引数を受け取るので、外部クラスからwith()の引数で直接値を指定することができる。
 * 
 * 例：
 * class Sample{
	 public static void main(String[] args){
		 TodoInfo info = new TodoInfo.Builder(100)
									 .with(todo -> {
										todo.status = "finish";
										todo.classification = "work";
										todo.task = "create an Activity";
										todo.description = "aaa";
										todo.createDateTime = LocalDateTime.now();
										todo.updateDateTime = LocalDateTime.now();
										todo.creator = "reki";
								   }).build();
		 System.out.println(info);
	 }
	}
 * 
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
	private boolean isFavorite;
	
	/*
	 * Builder型のパラメータを受け取るので、
	 */
	private TodoInfo(Builder builder) {
		this.id = builder.id;
		this.status = builder.status;
		this.classification = builder.classification;
		this.task = builder.task;
		this.description = builder.description;
		this.createDateTime = builder.createDateTime;
		this.updateDateTime = builder.updateDateTime;
		this.creator = builder.creator;
		this.isFavorite = builder.isFavorite;
	}
	
	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getClassification() {
		return classification;
	}

	public String getTask() {
		return task;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public String getCreator() {
		return creator;
	}
	
	public boolean getIsFavorite() {
		return isFavorite;
	}

	@Override
	public String toString() {
		return id + ", " + status + ", " + classification + ", " + task + ", " + description + ", " + createDateTime
				+ ", " + updateDateTime + ", " + creator;
	}
	
	/*
	 * 
	 * TodoInfoクラスのインスタンスを生成するクラス
	 * 
	 */
	public static class Builder{
		public int id;
		public String status;
		public String classification;
		public String task;
		public String description;
		public LocalDateTime createDateTime;
		public LocalDateTime updateDateTime;
		public String creator;
		public boolean isFavorite;
		
		public Builder with(Consumer<Builder> consumer) {
			consumer.accept(this);
			return this;
		}
		
		public TodoInfo build() {
			return new TodoInfo(this);
		}
	}
}
