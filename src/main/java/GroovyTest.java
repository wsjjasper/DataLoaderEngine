import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.config.InputConfiguration;
import com.config.OutputConfiguration;
import com.config.OutputRule;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

//TODO rename
public class GroovyTest {

    private static List<Map<String, Object>> loanInputs = new ArrayList<>();
    private static List<Map<String, Object>> qeInputs = new ArrayList<>();
    private static OutputConfiguration outputConf;

    static {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("LOAN_ID", i);
            map.put("FUNDED_USD", 100 + i);
            map.put("COMMITMENT_USD", 1000 + i);
            loanInputs.add(map);
        }

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("LOAN_ID", i);
            map.put("FACTOR", 0.1 + i);
            qeInputs.add(map);
        }

        OutputRule[] rules = new OutputRule[4];
        rules[0] = new OutputRule("LOAN_ID", "String", null, "LOAN_INPUT.LOAN_ID", true, null, null, 1);
        rules[1] = new OutputRule("FUNDED_USD", "Double", null, "LOAN_INPUT.FUNDED_USD * 1.1", true, 100, null, 1);
        rules[2] = new OutputRule("COMMITMENT_USD", "Double", null, "LOAN_INPUT.COMMITMENT_USD", true, null, null, 1);
        rules[3] = new OutputRule("UNFUNDED_USD", "Double", null, "COMMITMENT_USD - FUNDED_USD", true, null, null, 1);
        String[] lookupColumns = {"LOAN_ID"};
        rules[3] = new OutputRule("QE_FUNDED_USD", "Double", null, "FUNDED_USD * QE_INPUT.FACTOR", true, null,
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

        outputConf = new OutputConfiguration("CI_OUTPUT_PERIOD", mainInput, otherInputs, rules);

    }

    public static void main(String[] args) {
        groovyTest();
    }

    public static void groovyTest() {
        List<Map<String, Object>> loanOutputs = new ArrayList<>();

        for (Map<String, Object> mainInput : loanInputs) {
            Map<String, Object> outputMap = new HashMap<>();
            Binding binding = new Binding();
            GroovyShell groovyShell = new GroovyShell(binding);
            for (OutputRule rule : outputConf.getRules()) {
                binding.setProperty("LOAN_INPUT", mainInput);
                binding.setProperty("QE_INPUT", lookupInputByCriteria(qeInputs, outputMap, rule, false));
                // TODO lookup other input
                Object result = groovyShell.evaluate(convertFormula(rule.getFormula()));
                if (result == null) {
                    result = rule.getDefaultValue();
                }
                outputMap.put(rule.getFieldName(), result);
                binding.setProperty(rule.getFieldName(), result);
                System.out.println(rule.getFieldName() + ": " + outputMap.get(rule.getFieldName()));
            }
            loanOutputs.add(outputMap);
        }

    }

    /**
     * lookupInputByCriteria
     * @param inputMaps
     * @param outputMap
     * @param rule
     * @param isAggregation
     * @return
     */
    private static Object lookupInputByCriteria(List<Map<String, Object>> inputMaps, Map<String, Object> outputMap,
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

    private static String convertFormula(String formula) {
//		return formula.replace(formula, "LOAN_INPUT.LOAN_ID");
        return formula;
    }

    public static void readFile() {

    }
}
