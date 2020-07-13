package com.config;

public class OutputRule {
	private final String fieldName;
	private final String fieldType;
	private final String match;
	private final String formula;
	private final boolean isPersistence;
	private final Object defaultValue;
	private final String[] lookupColumns;
	private final int Sequence;
		
	public OutputRule(String fieldName, String fieldType, String match, String formula, boolean isPersistence,
			Object defaultValue, String[] lookupColumns, int sequence) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.match = match;
		this.formula = formula;
		this.isPersistence = isPersistence;
		this.defaultValue = defaultValue;
		this.lookupColumns = lookupColumns;
		Sequence = sequence;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public String getMatch() {
		return match;
	}
	public String getFormula() {
		return formula;
	}
	public boolean isPersistence() {
		return isPersistence;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public String[] getLookupColumns() {
		return lookupColumns;
	}
	public int getSequence() {
		return Sequence;
	}
	
	
}
