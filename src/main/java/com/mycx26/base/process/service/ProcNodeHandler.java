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

    @Resource
    protected ProcUpdateService procUpdateService;

    /**
     * approve validate
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void approveValidate(ApproveWrapper approveWrapper);

    /**
     * approve handle process main form data
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void handleMainForm(ApproveWrapper approveWrapper);

    /**
     * approve handle process sub form data
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void handleSubForm(ApproveWrapper approveWrapper);

    /**
     * approve handle business form data
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void handleBizForm(ApproveWrapper approveWrapper);

    /**
     * approve handle process node variables and return
     *
     * @param approveWrapper approve wrapper
     * @return variables
     */
    public abstract Map<String, Object> handleVars(ApproveWrapper approveWrapper);

    /**
     * Asynchronous callback after process approve.
     * The execution failure should not affect the main process.
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void afterApprove(ApproveWrapper approveWrapper);

    /**
     * reject previous data handle
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void rejectPreviousHandle(ApproveWrapper approveWrapper);

    /**
     * reject first data handle
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void rejectFirstHandle(ApproveWrapper approveWrapper);

    /**
     * reassign validate
     *
     * @param approveWrapper approve wrapper
     */
    public abstract void reassignValidate(ApproveWrapper approveWrapper);

    // ==============================================================>

    protected Map<String, Object> getMainFormByFlowNo(ApproveWrapper approveWrapper) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), approveWrapper.getFlowNo());

        return procFormService.getMainForm(approveWrapper.getProcDef().getMainForm(), clauses);
    }

    protected List<Map<String, Object>> getSubItemsByFlowNo(ApproveWrapper approveWrapper) {
        return getSubItemsByFlowNo(approveWrapper, null);
    }

    protected List<Map<String, Object>> getSubItemsByFlowNo(ApproveWrapper approveWrapper, Map<String, Object> adds) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), approveWrapper.getFlowNo());

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(approveWrapper.getProcDef().getSubForm(), clauses);
    }

    protected Map<String, Object> getMainFormByProcInstId(ApproveWrapper approveWrapper) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), approveWrapper.getProcInstId());

        return procFormService.getMainForm(approveWrapper.getProcDef().getMainForm(), clauses);
    }

    protected List<Map<String, Object>> getSubItemsByProcInstId(ApproveWrapper approveWrapper) {
        return getSubItemsByProcInstId(approveWrapper, null);
    }

    protected List<Map<String, Object>> getSubItemsByProcInstId(ApproveWrapper approveWrapper, Map<String, Object> adds) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), approveWrapper.getProcInstId());

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(approveWrapper.getProcDef().getSubForm(), clauses);
    }

    protected void updateByFlowNo(ApproveWrapper approveWrapper, String tblName, Map<String, Object> updates) {
        procUpdateService.updateByFlowNo(approveWrapper.getFlowNo(), tblName, updates);
    }

    protected void updateById(Long id, String tblName, Map<String, Object> updates) {
        procUpdateService.updateById(id, tblName, updates);
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
}
