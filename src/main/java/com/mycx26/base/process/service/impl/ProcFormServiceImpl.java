package com.mycx26.base.process.service.impl;

import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcColumn;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcColumnService;
import com.mycx26.base.process.service.ProcFormService;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.process.service.validate.ProcCreateMainValidator;
import com.mycx26.base.process.service.validate.ProcCreateSubValidator;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.Iterables;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mycx26 on 2020/6/22.
 */
@Service
public class ProcFormServiceImpl implements ProcFormService {

    @Resource
    private ProcColumnService procColumnService;

    @Resource
    private JdbcService jdbcService;

    @Override
    public void addMainForm(String mainForm, ProcParamWrapper procParamWrapper) {
        if (StringUtil.isBlank(mainForm)
                || CollectionUtil.isEmpty(procParamWrapper.getMainForm())) {
            throw new ParamException("Main form name and data are required");
        }

        List<ProcColumn> cols = procColumnService.getAddsByTblName(mainForm);
        if (cols.isEmpty()) {
            throw new DataException("Form columns config error: "  + mainForm);
        }

        Map<String, Object> mainFormMap = procParamWrapper.getMainForm();
        // general put flow number
        mainFormMap.put(ProcConstant.FLOW_NO, procParamWrapper.getFlowNo());
        mainFormRequiredValidate(mainFormMap, cols);

        ProcBaseService procBaseService = SpringUtil.getBean2(procParamWrapper.getProcDefKey() + ProcBaseService.SUFFIX);
        if (procBaseService instanceof ProcCreateMainValidator) {
            ((ProcCreateMainValidator)procBaseService).validateMainForm(procParamWrapper);
        }

        List<String> colNames = new ArrayList<>(cols.size());
        List<Object> values = new ArrayList<>(cols.size());
        cols.forEach(e -> {
            Object value = mainFormMap.get(e.getPropName());
            colNames.add(e.getColName());
            if (StringUtil.EMPTY.equals(value)) {
                value = null;
            }
            values.add(value);
        });

        jdbcService.insert(mainForm, colNames, values);
    }

    private void mainFormRequiredValidate(Map<String, Object> mainFormMap, List<ProcColumn> cols) {
        StringBuilder error = new StringBuilder();

        cols.forEach(e -> {
            Object value = mainFormMap.get(e.getPropName());
            if (e.getRequired() && (null == value
                    || (value instanceof String && StringUtil.isBlank((String) value)))) {
                StringUtil.append(error, e.getPropName() + " is required");
            }
        });

        if (error.length() > 0) {
            error.delete(error.length() - 1, error.length());
            throw new ParamException(error.toString());
        }
    }

    @Override
    public void addSubForm(String subForm, ProcParamWrapper procParamWrapper) {
        if (StringUtil.isBlank(subForm)) {
            throw new ParamException("Sub form name are required");
        }
        if (CollectionUtil.isEmpty(procParamWrapper.getSubItems())) {
            return;
        }

        List<ProcColumn> cols = procColumnService.getAddsByTblName(subForm);
        if (cols.isEmpty()) {
            throw new DataException("Form columns config error: "  + subForm);
        }

        // required validate
        List<Map<String, Object>> subItems = procParamWrapper.getSubItems();
        subFormRequiredValidate(subItems, cols, procParamWrapper.getFlowNo());

        // business validate
        ProcBaseService procBaseService = SpringUtil.getBean2(procParamWrapper.getProcDefKey() + ProcBaseService.SUFFIX);
        if (procBaseService instanceof ProcCreateSubValidator) {
            ((ProcCreateSubValidator)procBaseService).validateSubForm(procParamWrapper);
        }

        subItems = procParamWrapper.getSubItems();
        List<List<Object>> valueList = new ArrayList<>(subItems.size());
        Iterables.foreach(subItems, (i, item) -> {
            List<Object> values = new ArrayList<>(cols.size());
            cols.forEach(col -> {
                Object value = item.get(col.getPropName());
                if (StringUtil.EMPTY.equals(value)) {
                    value = null;
                }
                values.add(value != null ? value.toString() : null);
            });
            valueList.add(values);
        });

        jdbcService.insertBatch(subForm, cols.stream().map(ProcColumn::getColName).collect(Collectors.toList()), valueList);
    }

    private void subFormRequiredValidate(List<Map<String, Object>> subItems, List<ProcColumn> cols, String flowNo) {
        StringBuilder error = new StringBuilder();
        Iterables.foreach(subItems, (i, item) -> {
            // general put flow number
            item.put(ProcConstant.FLOW_NO, flowNo);
            cols.forEach(col -> {
                Object value = item.get(col.getPropName());
                if (col.getRequired() && (null == value
                        || (value instanceof String && StringUtil.isBlank((String) value)))) {
                    StringUtil.append(error, (i + 1) + " row " + col.getPropName() + " is required");
                }
            });
        });
        if (error.length() > 0) {
            error.delete(error.length() - 1, error.length());
            throw new ParamException(error.toString());
        }
    }

    @Override
    public List<Map<String, Object>> getSubForm(String subForm, Map<String, Object> clauses) {
        if (StringUtil.isBlank(subForm) || CollectionUtil.isEmpty(clauses)) {
            return Collections.emptyList();
        }

        List<ProcColumn> cols = procColumnService.getByTblName(subForm);
        if (cols.isEmpty()) {
            return Collections.emptyList();
        }

        return jdbcService.selectList(subForm, cols.stream().map(ProcColumn::getColName).collect(Collectors.toList()), clauses);
    }

    @Override
    public Map<String, Object> getMainForm(String mainForm, Map<String, Object> clauses) {
        if (StringUtil.isBlank(mainForm) || CollectionUtil.isEmpty(clauses)) {
            return null;
        }

        List<ProcColumn> cols = procColumnService.getByTblName(mainForm);
        if (cols.isEmpty()) {
            return null;
        }

        return jdbcService.selectMap(mainForm, cols.stream().map(ProcColumn::getColName).collect(Collectors.toList()), clauses);
    }
}
