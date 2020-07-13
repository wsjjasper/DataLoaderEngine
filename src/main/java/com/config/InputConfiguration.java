package com.config;

public class InputConfiguration {
	private final String moduleName;
	private final String[] spilt;
	private final String[][] columns;
	private final boolean isMainInput;
		
	public InputConfiguration(String moduleName, String[] spilt, String[][] columns, boolean isMainInput) {
		super();
		this.moduleName = moduleName;
		this.spilt = spilt;
		this.columns = columns;
		this.isMainInput = isMainInput;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	public String[] getSpilt() {
		return spilt;
	}
	public String[][] getColumns() {
		return columns;
	}
	public boolean isMainInput() {
		return isMainInput;
	}
	
	
}
