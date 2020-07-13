package com.config;

public class OutputConfiguration {
	private final String outputModule;
	private final InputConfiguration mainInput;
	private final InputConfiguration[] inputConfs;
	private final OutputRule[] rules;
	
	public OutputConfiguration(String outputModule, InputConfiguration mainInput, InputConfiguration[] inputConfs,
			OutputRule[] rules) {
		super();
		this.outputModule = outputModule;
		this.mainInput = mainInput;
		this.inputConfs = inputConfs;
		this.rules = rules;
	}
	
	public String getOutputModule() {
		return outputModule;
	}
	public InputConfiguration getMainInput() {
		return mainInput;
	}
	public InputConfiguration[] getInputConfs() {
		return inputConfs;
	}
	public OutputRule[] getRules() {
		return rules;
	}
	
	
}
