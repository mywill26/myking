package com.mycx26.base.process.service;

import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SqlUtil;
import com.mycx26.base.util.StringUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mycx26 on 2020/6/29.
 */
public abstract class ProcNodeHandler {

    @Resource
    protected JdbcService jdbcService;

    @Resource
    protected ProcFormService procFormService;

    @Resource
    protected ProcQueryService procQueryService;

    @Resource
    protected ProcLockService procLockService;

    @Resource
    protected ProcInstService procInstService;

    // approve validate
    public abstract void approveValidate(ApproveWrapper approveWrapper);

    // approve handle process main form data
    public abstract void handleMainForm(ApproveWrapper approveWrapper);

    // approve handle process sub form data
    public abstract void handleSubForm(ApproveWrapper approveWrapper);

    // approve handle business form data
    public abstract void handleBizForm(ApproveWrapper approveWrapper);

    // approve handle process node variables and return
    public abstract Map<String, Object> handleVars(ApproveWrapper approveWrapper);

    // reject previous data handle
    public abstract void rejectPreviousHandle(ApproveWrapper approveWrapper);

    // reject first data handle
    public abstract void rejectFirstHandle(ApproveWrapper approveWrapper);

    // ==============================================================>

    protected Map<String, Object> getMainFormByFlowNo(ApproveWrapper approveWrapper) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), approveWrapper.getFlowNo());

        return procFormService.getMainForm(approveWrapper.getProcDef().getMainForm(), clauses);
    }

    protected List<Map<String, Object>> getSubItemsByFlowNo(ApproveWrapper approveWrapper, Map<String, Object> adds) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), approveWrapper.getFlowNo());

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(approveWrapper.getProcDef().getSubForm(), clauses);
    }

    protected void updateByFlowNo(ApproveWrapper approveWrapper, String tblName, Map<String, Object> updates) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), approveWrapper.getFlowNo());

        jdbcService.update(tblName, updates, clauses);
    }

    protected void updateById(Long id, String tblName, Map<String, Object> updates) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.ID), id);

        jdbcService.update(tblName, updates, clauses);
    }

    protected void doUnlock(ApproveWrapper approveWrapper) {
        ProcDef procDef = approveWrapper.getProcDef();
        if (procDef.getLockResource()) {
            List<Map<String, Object>> subItems = getSubItemsByFlowNo(approveWrapper, null);
            if (subItems.isEmpty()) {
                return;
            }

            String resourceKey = procDef.getResourceKey();
            if (StringUtil.isNotBlank(resourceKey)) {
                List<String> resourceIds = subItems.stream()
                        .filter(e -> StringUtil.isNotBlank((String) e.get(resourceKey)))
                        .map(e -> (String) e.get(resourceKey))
                        .collect(Collectors.toList());
                procLockService.unlock(resourceIds);
            }
        }
    }

    protected Map<String, Object> getMainFormByProcInstId(ApproveWrapper approveWrapper) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), approveWrapper.getProcInstId());

        return procFormService.getMainForm(approveWrapper.getProcDef().getMainForm(), clauses);
    }
}
