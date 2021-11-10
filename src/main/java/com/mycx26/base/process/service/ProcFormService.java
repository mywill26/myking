package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/6/22.
 */
public interface ProcFormService {

    /**
     * add main form
     *
     * @param mainForm main form name
     * @param procParamWrapper process parameter wrapper
     */
    void addMainForm(String mainForm, ProcParamWrapper procParamWrapper);

    /**
     * add sub form, include all sub items
     *
     * @param subForm sub form name
     * @param procParamWrapper process parameter wrapper
     */
    void addSubForm(String subForm, ProcParamWrapper procParamWrapper);

    /**
     * get sub sub form by clauses
     *
     * @param subForm sub form name
     * @param clauses clauses
     * @return sub form
     */
    List<Map<String, Object>> getSubForm(String subForm, Map<String, Object> clauses);

    /**
     * get main form by clauses
     *
     * @param mainForm main form name
     * @param clauses clauses
     * @return main form
     */
    Map<String, Object> getMainForm(String mainForm, Map<String, Object> clauses);
}
