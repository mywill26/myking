package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/6/22.
 */
public interface ProcFormService {

    void addMainForm(String mainForm, ProcParamWrapper procParamWrapper);

    void addSubForm(String subForm, ProcParamWrapper procParamWrapper);

    // single sub form
    List<Map<String, Object>> getSubForm(String subForm, Map<String, Object> clauses);

    // single main form
    Map<String, Object> getMainForm(String mainForm, Map<String, Object> clauses);
}
