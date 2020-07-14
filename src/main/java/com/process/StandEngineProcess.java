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

public class StandEngineProcess extends BaseEngineProcess {


    @Override
    public void init() {
        OutputRule[] rules = new OutputRule[5];
        rules[0] = new OutputRule("LOAN_ID", "String", null, "LOAN_INPUT.LOAN_ID", true, null, null, 1);
        rules[1] = new OutputRule("FUNDED_USD", "Double", null, "LOAN_INPUT.FUNDED_USD * 1.1", true, 100, null, 1);
        rules[2] = new OutputRule("COMMITMENT_USD", "Double", "1 > 2", "LOAN_INPUT.COMMITMENT_USD", true, 1000, null, 1);
        rules[3] = new OutputRule("UNFUNDED_USD", "Double", null, "COMMITMENT_USD - FUNDED_USD", true, null, null, 1);
        String[] lookupColumns = {"LOAN_ID"};
        rules[4] = new OutputRule("QE_FUNDED_USD", "Double", null, "FUNDED_USD * QE_INPUT.FACTOR", true, null,
                lookupColumns, 1);

        String[][] mainInputColumns = new String[3][3];
        mainInputColumns[0][0] = "LOAN_ID";
        mainInputColumns[0][1] = "String";
        mainInputColumns[1][0] = "FUNDED_USD";
        mainInputColumns[1][1] = "Double";
        mainInputColumns[1][0] = "COMMITMENT_USD";
        mainInputColumns[1][1] = "Double";
        InputConfiguration mainInput = new InputConfiguration("LOAN_INPUT", null, mainInputColumns, true);

        String[][] otherInputColumns = new String[2][2];
        otherInputColumns[0][0] = "LOAN_ID";
        otherInputColumns[0][1] = "String";
        otherInputColumns[1][0] = "FACTOR_FUNDED";
        otherInputColumns[1][1] = "Double";
        InputConfiguration otherInput = new InputConfiguration("QE_INPUT", null, otherInputColumns, false);
        InputConfiguration[] otherInputs = new InputConfiguration[1];
        otherInputs[0] = otherInput;

        EngineConfiguration outputConf = new EngineConfiguration("CI_OUTPUT_PERIOD", mainInput, otherInputs, rules);
        List<EngineConfiguration> engineConfigurationList = new ArrayList<>();
        engineConfigurationList.add(outputConf);

        setEngineConfigurationList(engineConfigurationList);
    }

    public static void main(String[] args) {
        StandEngineProcess process = new StandEngineProcess();
        process.start();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }

    @Override
    protected void loadInputData() {
        List<Map<String, Object>> mainInput = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("LOAN_ID", i);
            map.put("FUNDED_USD", 100 + i);
            map.put("COMMITMENT_USD", 1000 + i);
            mainInput.add(map);
        }
        setMainInput(mainInput);

        List<Map<String, Object>> qeInputs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("LOAN_ID", i);
            map.put("FACTOR", 0.1 + i);
            qeInputs.add(map);
        }
        addOtherInput("QE_INPUT", qeInputs);
    }
}
