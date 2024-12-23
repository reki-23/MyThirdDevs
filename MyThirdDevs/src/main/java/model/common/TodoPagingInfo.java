package model.common;

import java.util.List;

/*
 * 
 * ページング処理で使用するデータを管理するDTOクラス
 * 
 */
public class TodoPagingInfo<T> {
	
	private List<T> pageByPageListData;
	private int totalPageCount;
	private int currentPage;
	
	public TodoPagingInfo(List<T> pageByPageListData, int totalPageCount, int currentPage) {
		this.pageByPageListData = pageByPageListData;
		this.totalPageCount = totalPageCount;
		this.currentPage = currentPage;
	}

	public List<T> getPageByPageListData() {
		return pageByPageListData;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}
}
