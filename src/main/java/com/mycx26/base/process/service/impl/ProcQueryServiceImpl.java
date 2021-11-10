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
import com.mycx26.base.process.service.bo.FullWrapper;
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
    public ProcDef getProcDefByDefKey(String procDefKey) {
        return procDefService.getByKey(procDefKey);
    }

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
    public ProcInst getProcInstByFlowNo(String flowNo) {
        return procInstService.getByFlowNo(flowNo);
    }

    @Override
    public ProcInst getProcInstByInstId(String procInstId){
        return procInstService.getByProcInstId(procInstId);
    }

    @Override
    public Map<String, Object> getMainFormByFlowNo(String flowNo) {
        ProcInst procInst = getProcInstByFlowNo(flowNo);
        return getMainFormByFlowNo(procInst.getProcDefKey(), flowNo);
    }

    @Override
    public Map<String, Object> getMainFormByFlowNo(String procDefKey, String flowNo) {
        ProcDef procDef = getProcDefByDefKey(procDefKey);
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        return procFormService.getMainForm(procDef.getMainForm(), clauses);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByFlowNo(String flowNo) {
        return getSubItemsByFlowNo(flowNo, null);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByFlowNo(String flowNo, Map<String, Object> adds) {
        ProcInst procInst = getProcInstByFlowNo(flowNo);
        return getSubItemsByFlowNo(procInst.getProcDefKey(), flowNo, adds);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByFlowNo(String procDefKey, String flowNo, Map<String, Object> adds) {
        ProcDef procDef = getProcDefByDefKey(procDefKey);

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(procDef.getSubForm(), clauses);
    }

    @Override
    public Map<String, Object> getMainFormByProcInstId(String procInstId) {
        ProcInst procInst = getProcInstByInstId(procInstId);
        return getMainFormByProcInstId(procInst.getProcDefKey(), procInstId);
    }

    @Override
    public Map<String, Object> getMainFormByProcInstId(String procDefKey, String procInstId) {
        ProcDef procDef = getProcDefByDefKey(procDefKey);
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInstId);

        return procFormService.getMainForm(procDef.getMainForm(), clauses);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByProcInstId(String procInstId) {
        return getSubItemsByProcInstId(procInstId, null);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByProcInstId(String procInstId, Map<String, Object> adds) {
        ProcInst procInst = getProcInstByInstId(procInstId);
        return getSubItemsByProcInstId(procInst.getProcDefKey(), procInstId, adds);
    }

    @Override
    public List<Map<String, Object>> getSubItemsByProcInstId(String procDefKey, String procInstId, Map<String, Object> adds) {
        ProcDef procDef = getProcDefByDefKey(procDefKey);

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInstId);

        if (!CollectionUtil.isEmpty(adds)) {
            clauses.putAll(adds);
        }

        return procFormService.getSubForm(procDef.getSubForm(), clauses);
    }

    @Override
    public ProcNode getProcNodeByDefKeyAndNodeKey(String procDefKey, String nodeKey) {
        return procNodeService.getByProcDefKeyAndNodeKey(procDefKey, nodeKey);
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

    @Override
    public FullWrapper buildFullByInstId(String procInstId) {
        FullWrapper fullWrapper = new FullWrapper();

        ProcInst procInst = getProcInstByInstId(procInstId);
        fullWrapper.setProcInst(procInst);

        fullWrapper.setMainForm(getMainFormByProcInstId(procInst.getProcDefKey(), procInstId));
        fullWrapper.setSubItems(getSubItemsByProcInstId(procInst.getProcDefKey(), procInstId, null));

        return fullWrapper;
    }

    @Override
    public FullWrapper buildFullFlowNo(String flowNo) {
        FullWrapper fullWrapper = new FullWrapper();

        ProcInst procInst = getProcInstByFlowNo(flowNo);
        fullWrapper.setProcInst(procInst);

        fullWrapper.setMainForm(getMainFormByFlowNo(procInst.getProcDefKey(), flowNo));
        fullWrapper.setSubItems(getSubItemsByFlowNo(procInst.getProcDefKey(), flowNo, null));

        return fullWrapper;
    }
}
