package com.owitho.www.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.owitho.www.enums.OperationType;

/**
 * @DESC data entity
 * @author xinshiyou
 */
public class DataEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public String dbName = null;// database name
	public String tabName = null;// table name
	public OperationType type = null;// SQL[create,drop] or delete,insert,update
	public String sql = null;// create or drop table SQL
	public String cols = null;// columns
	public List<?> rows = null;// rows data

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}