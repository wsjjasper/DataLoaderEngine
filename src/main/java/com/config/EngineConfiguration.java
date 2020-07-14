package com.config;

import java.util.List;
import java.util.Map;

public class EngineConfiguration {
	private final String outputModule;
	private final InputConfiguration mainInput;
	private final InputConfiguration[] inputConfs;
	private final OutputRule[] rules;
	private List<Map<String, Object>> outputs;
	
	public EngineConfiguration(String outputModule, InputConfiguration mainInput, InputConfiguration[] inputConfs,
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

	public List<Map<String, Object>> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Map<String, Object>> outputs) {
		this.outputs = outputs;
	}
}
