package com.owitho.www.enums;

/**
 * @DESC operation type
 * @author xinshiyou
 */
public enum OperationType {

	INSERT("insert"), DELETE("delete"), UPDATE("update"), SQL("sql");

	private String name;

	private OperationType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
