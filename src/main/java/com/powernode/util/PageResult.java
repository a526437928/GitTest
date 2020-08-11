package com.powernode.util;

import java.util.List;

public class PageResult {

	//总记录数
	private Integer total;

	//当前页面结果
	private List<?> rows;
	
	public PageResult(Integer total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public PageResult(long total, List<?> rows) {
		this.total = (int) total;
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	
}
