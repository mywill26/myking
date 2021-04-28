package com.mycx26.base.process.service;

import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.bo.FullWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.util.SqlUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/6/24.
 */
public abstract class ProcBaseService {

    public static final String SUFFIX = "Service";

    @Resource
    protected ProcInstService procInstService;

    @Resource
    protected ProcDefService procDefService;

    @Resource
    protected ProcFormService procFormService;

    @Resource
    protected ProcQueryService procQueryService;

    // set process start variables and return
    public abstract Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper);

    // process end postHandle
    public abstract void endPostHandle(String procInstId);

    /**
     * Called after the process is rejected to first node.
     *
     * @param procInstId process instance id
     */
    public abstract void rejectFirstHandle(String procInstId);

    protected Map<String, Object> getMainFormByFlowNo(String procDefKey, String flowNo) {
        ProcDef procDef = procDefService.getByKey(procDefKey);
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        return procFormService.getMainForm(procDef.getMainForm(), clauses);
    }

    protected Map<String, Object> getMainFormByProcInstId(String procInstId) {
        return procQueryService.getMainFormByProcInstId(procInstId);
    }

    protected List<Map<String, Object>> getSubItemsByProcInstId(String procInstId, Map<String, Object> adds) {
        return procQueryService.getSubItemsByProcInstId(procInstId, adds);
    }

    protected FullWrapper buildFull(String procInstId) {
        FullWrapper fullWrapper = new FullWrapper();

        ProcInst procInst = procInstService.getByProcInstId(procInstId);

        fullWrapper.setProcInst(procInst);
        fullWrapper.setMainForm(getMainFormByFlowNo(procInst.getProcDefKey(), procInst.getFlowNo()));

        return fullWrapper;
    }
}
