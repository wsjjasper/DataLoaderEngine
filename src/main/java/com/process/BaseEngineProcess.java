package com.process;

import com.config.EngineConfiguration;
import com.config.InputConfiguration;
import com.config.OutputRule;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseEngineProcess {

    private List<Map<String, Object>> mainInput = new ArrayList<>();
    private Map<String, List<Map<String, Object>>> inputMap = new HashMap<>();
    private List<EngineConfiguration> engineConfigurationList = new ArrayList<>();

    public final void start() {
        try {
            init();
            loadInputData();
            if (validate()) {
                onProcess();
            } else {
                onError();
            }
        } catch (Exception e) {
            onError();
            throw e;
        }
        onSuccess();
    }

    protected void onProcess() {
        for (EngineConfiguration outputConf : engineConfigurationList) {
            List<Map<String, Object>> loanOutputs = new ArrayList<>();
            //Iterate main input
            Map<String, Object> outputMap;
            Binding binding;
            GroovyShell groovyShell;
            for (Map<String, Object> mainInput : mainInput) {
                outputMap = new HashMap<>();
                binding = new Binding();
                groovyShell = new GroovyShell(binding);
                for (OutputRule rule : outputConf.getRules()) {
                    //Bind main input
                    binding.setProperty(outputConf.getMainInput().getModuleName(), mainInput);
                    //Bind other inputs if exists
                    if (outputConf.getInputConfs() != null && outputConf.getInputConfs().length > 0) {
                        for (InputConfiguration inputConf : outputConf.getInputConfs()) {
                            String inputModuleName = inputConf.getModuleName();
                            binding.setProperty(inputModuleName, lookupInputByCriteria(inputMap.get(inputModuleName), outputMap, rule, false));
                        }
                    }
                    //Add match if
                    String formula = addMatch(rule.getMatch(), convertFormula(rule.getFormula()));
                    //Execute formula
                    System.out.println("Formula is: " + formula);
                    Object result = groovyShell.evaluate(formula);
                    if (result == null) {
                        result = rule.getDefaultValue();
                    }
                    outputMap.put(rule.getFieldName(), result);
                    binding.setProperty(rule.getFieldName(), result);
                    System.out.println("Result is: " + rule.getFieldName() + " = " + outputMap.get(rule.getFieldName()));
                }
                loanOutputs.add(outputMap);
            }
        }
    }

    /**
     * Load configuration of input and output
     */
    protected abstract void init();

    protected abstract void onError();

    /**
     * Load input data
     */
    protected abstract void loadInputData();

    protected abstract void onSuccess();

    protected boolean validate() {
        return true;
    }

    ;

    public Object lookupInputByCriteria(List<Map<String, Object>> inputMaps, Map<String, Object> outputMap,
                                        OutputRule rule, boolean isAggregation) {
        String[] lookupColumns = rule.getLookupColumns();
        if (lookupColumns == null || lookupColumns.length == 0) {
            return null;
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> map : inputMaps) {
            boolean isMatch = true;
            for (String column : lookupColumns) {
                if (map.get(column) == null || !map.get(column).equals(outputMap.get(column))) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                if (!isAggregation) {
                    return map;
                }
                results.add(map);
            }
        }
        return results;
    }

    public String convertFormula(String formula) {
        return formula;
    }

    public String addMatch(String match, String formula) {
        if (match != null && !"".equals(match.trim())) {
            return "if( " + match + "){" + formula + "}";
        }
        return formula;
    }

    public void addOtherInput(String inputModuleName, List<Map<String, Object>> inputData) {
        //TODO duplicate check, give error or warning
        this.inputMap.put(inputModuleName, inputData);
    }

    public List<Map<String, Object>> getMainInput() {
        return mainInput;
    }

    public void setMainInput(List<Map<String, Object>> mainInput) {
        this.mainInput = mainInput;
    }

    public Map<String, List<Map<String, Object>>> getInputMap() {
        return inputMap;
    }

    public void setInputMap(Map<String, List<Map<String, Object>>> inputMap) {
        this.inputMap = inputMap;
    }

    public List<EngineConfiguration> getEngineConfigurationList() {
        return engineConfigurationList;
    }

    public void setEngineConfigurationList(List<EngineConfiguration> engineConfigurationList) {
        this.engineConfigurationList = engineConfigurationList;
    }
}
