package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.table.TableReflectionUtil;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link UiTableValidator} responsible for validating table data and elements.
 * <p>
 * This class provides methods for extracting table row values, table elements, and performing
 * assertions on tables based on different validation targets.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
//todo check spring somehow as the others to add allure
@NoArgsConstructor
public class UiTableValidatorImpl implements UiTableValidator {

    /**
     * Logs the assertion target details for debugging purposes.
     *
     * @param data The assertion target data to be logged.
     */
    protected void printAssertionTarget(Map<String, Object> data) {
        LogUI.extended("Validation target: [{}]", data.toString());
    }

    /**
     * Validates the given table object against a set of assertions.
     * <p>
     * Extracts data based on the assertion target (row values, row elements, table values, or table elements),
     * logs the validation target, and executes assertions on the extracted data.
     * </p>
     *
     * @param object     The table object to validate.
     * @param assertions The assertions to apply.
     * @param <T>        The expected type of assertion results.
     * @return A list of assertion results indicating whether the validation passed or failed.
     */
    @Override
    public <T> List<AssertionResult<T>> validateTable(final Object object, final Assertion... assertions) {
        startValidation(assertions.length);

        Map<String, T> data = new HashMap<>();

        for (Assertion assertion : assertions) {
            processAssertion((UiTablesAssertionTarget) assertion.getTarget());
            switch ((UiTablesAssertionTarget) assertion.getTarget()) {
                case ROW_VALUES -> {
                    data.put("rowValues", (T) TableReflectionUtil.extractTextsFromRow(object));
                    assertion.setKey("rowValues");
                }
                case ROW_ELEMENTS -> {
                    data.put("rowElements", (T) TableReflectionUtil.extractElementsFromRow(object));
                    assertion.setKey("rowElements");
                }
                case TABLE_VALUES -> {
                    List<?> rows = (List<?>) object;
                    List<List<String>> rowList = rows.stream().map(TableReflectionUtil::extractTextsFromRow).toList();
                    data.put("tableValues", (T) rowList);
                    assertion.setKey("tableValues");
                }
                case TABLE_ELEMENTS -> {
                    List<?> rows = (List<?>) object;
                    List<List<SmartWebElement>> rowList = rows.stream().map(TableReflectionUtil::extractElementsFromRow).toList();
                    data.put("tableElements", (T) rowList);
                    assertion.setKey("tableElements");
                }
            }
        }

        printAssertionTarget((Map<String, Object>) data);
        return AssertionUtil.validate(data, List.of(assertions));
    }


    /**
     * Logs the beginning of a validation process with a specified number of assertions.
     * <p>
     * This method provides an Allure step annotation for better test reporting
     * and logs the validation start information using {@link LogUI}.
     * </p>
     *
     * @param assertionCount the number of assertions that will be performed
     */
    public void startValidation(int assertionCount) {
        LogUI.info("Starting response validation with {} assertion(s).", assertionCount);
    }

    /**
     * Logs and processes an assertion for a specified UI table target.
     * <p>
     * This method marks the assertion processing step in Allure reports
     * and logs the assertion target using {@link LogUI}.
     * </p>
     *
     * @param target the UI table assertion target being processed
     */
    public void processAssertion(UiTablesAssertionTarget target) {
        LogUI.info("Processing assertion for target: {}", target);
    }

}
