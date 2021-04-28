package com.mycx26.base.process.service.impl;

import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcFormService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SqlUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/6/15.
 */
@Service
public class ProcQueryServiceImpl implements ProcQueryService {

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private ProcNodeService procNodeService;

    @Resource
    private ProcFormService procFormService;

    @Override
    public ProcDef getProcDefByInstId(String procInstId) {
        ProcInst procInst = procInstService.getByProcInstId(procInstId);

        if (null == procInst) {
            throw new DataException("Process instance not exist");
        }

        return procDefService.getByKey(procInst.getProcDefKey());
    }

    @Override
    public ProcDef getProcDefByFlowNo(String flowNo) {
        ProcInst procInst = procInstService.getByFlowNo(flowNo);

        if (null == procInst) {
            throw new DataException("Process instance not exist");
        }

        return procDefService.getByKey(procInst.getProcDefKey());
    }

    @Override
    public String getViewKeyByProcInstIdAndNodeKey(String procInstId, String nodeKey) {
        if (StringUtil.isAnyBlank(procInstId, nodeKey)) {
            return null;
        }

        ProcDef procDef = getProcDefByInstId(procInstId);

        return procNodeService.getViewKeyByProcDefKeyAndNodeKey(procDef.getProcDefKey(), nodeKey);
    }

    @Override
    public String getDetailViewKeyByProcInstId(String procInstId) {
        if (StringUtil.isBlank(procInstId)) {
            return null;
        }

        return getProcDefByInstId(procInstId).getDetailViewKey();
    }

    @Override
    public String getDetailViewKeyByFlowNo(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return null;
        }

        return getProcDefByFlowNo(flowNo).getDetailViewKey();
    }

    @Override
    public ProcInst getProcInstByInstId(String procInstId){
        return procInstService.getByProcInstId(procInstId);
    }

    @Override
    public Map<String, Object> getMainFormByFlowNo(String flowNo) {
        String mainForm = getMainFormNameByFlowNo(flowNo);
        if (StringUtil.isBlank(mainForm)) {
            return null;
        }

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        return procFormService.getMainForm(mainForm, clauses);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByFlowNo(String flowNo) {
        String subForm = getSubFormNameByFlowNo(flowNo);
        if (StringUtil.isBlank(subForm)) {
            return null;
        }

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        return procFormService.getSubForm(subForm, clauses);
    }

    @Override
    public Map<String, Object> getMainFormByProcInstId(String procInstId) {
        ProcDef procDef = getProcDefByInstId(procInstId);
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInstId);

        return procFormService.getMainForm(procDef.getMainForm(), clauses);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByProcInstId(String procInstId, Map<String, Object> adds) {
        ProcDef procDef = getProcDefByInstId(procInstId);

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInstId);

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(procDef.getSubForm(), clauses);
    }

    @Override
    public String getMainFormNameByFlowNo(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return null;
        }

        return getProcDefByFlowNo(flowNo).getMainForm();
    }

    @Override
    public String getSubFormNameByFlowNo(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return null;
        }

        return getProcDefByFlowNo(flowNo).getSubForm();
    }

    @Override
    public ProcNode getProcNodeByFlowNoAndNodeKey(String flowNo, String nodeKey) {
        if (StringUtil.isAnyBlank(flowNo, nodeKey)) {
            return null;
        }

        ProcDef procDef = getProcDefByFlowNo(flowNo);
        ProcNode procNode = procNodeService.getByProcDefKeyAndNodeKey(procDef.getProcDefKey(), nodeKey);
        if (null == procNode) {
            throw new ParamException("Process node not exist");
        }

        return procNode;
    }

    @Override
    public ProcNode getProcNodeByInstIdAndNodeKey(String procInstId, String nodeKey) {
        if (StringUtil.isAnyBlank(procInstId, nodeKey)) {
            return null;
        }

        ProcDef procDef = getProcDefByInstId(procInstId);
        ProcNode procNode = procNodeService.getByProcDefKeyAndNodeKey(procDef.getProcDefKey(), nodeKey);
        if (null == procNode) {
            throw new ParamException("Process node not exist");
        }

        return procNode;
    }
}
