package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;

import java.util.List;
import java.util.Map;

/**
 * Process common query service.
 *
 * Created by mycx26 on 2020/6/15.
 */
public interface ProcQueryService {

    ProcDef getProcDefByInstId(String procInstId);

    ProcDef getProcDefByFlowNo(String flowNo);

    String getViewKeyByProcInstIdAndNodeKey(String procInstId, String nodeKey);

    String getDetailViewKeyByProcInstId(String procInstId);

    String getDetailViewKeyByFlowNo(String flowNo);

    ProcInst getProcInstByInstId(String procInstId);

    Map<String, Object> getMainFormByFlowNo(String flowNo);

    List<Map<String, Object>> getSubItemsByFlowNo(String flowNo);

    Map<String, Object> getMainFormByProcInstId(String procInstId);

    List<Map<String, Object>> getSubItemsByProcInstId(String procInstId, Map<String, Object> adds);

    String getMainFormNameByFlowNo(String flowNo);

    String getSubFormNameByFlowNo(String flowNo);

    ProcNode getProcNodeByFlowNoAndNodeKey(String flowNo, String nodeKey);

    ProcNode getProcNodeByInstIdAndNodeKey(String procInstId, String nodeKey);
}
