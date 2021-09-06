package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.bo.FullWrapper;

import java.util.List;
import java.util.Map;

/**
 * Process common query service.
 *
 * Created by mycx26 on 2020/6/15.
 */
public interface ProcQueryService {

    ProcDef getProcDefByDefKey(String procDefKey);

    ProcDef getProcDefByInstId(String procInstId);

    ProcDef getProcDefByFlowNo(String flowNo);

    String getViewKeyByProcInstIdAndNodeKey(String procInstId, String nodeKey);

    String getDetailViewKeyByProcInstId(String procInstId);

    String getDetailViewKeyByFlowNo(String flowNo);

    ProcInst getProcInstByFlowNo(String flowNo);

    ProcInst getProcInstByInstId(String procInstId);

    /**
     * get main form by flow no
     *
     * @param flowNo flow no
     * @return main form
     */
    Map<String, Object> getMainFormByFlowNo(String flowNo);

    /**
     * get main form by process definition key and flow no
     *
     * @param procDefKey process definition key
     * @param flowNo flow no
     * @return main form
     */
    Map<String, Object> getMainFormByFlowNo(String procDefKey, String flowNo);

    /**
     * get sub items by flow no
     *
     * @param flowNo flow no
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByFlowNo(String flowNo);

    /**
     * get sub items by flow no and added clauses
     *
     * @param flowNo flow no
     * @param adds added clauses
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByFlowNo(String flowNo, Map<String, Object> adds);

    /**
     * get sub items by process definition key, flow no and added clauses
     *
     * @param procDefKey process definition key
     * @param flowNo flow no
     * @param adds added clauses
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByFlowNo(String procDefKey, String flowNo, Map<String, Object> adds);

    /**
     * get main form by process instance id
     *
     * @param procInstId process instance id
     * @return main form
     */
    Map<String, Object> getMainFormByProcInstId(String procInstId);

    /**
     * get main form by process definition key and process instance id
     *
     * @param procDefKey process definition key
     * @param procInstId process instance id
     * @return main form
     */
    Map<String, Object> getMainFormByProcInstId(String procDefKey, String procInstId);

    /**
     * get sub items by process instance id
     *
     * @param procInstId process instance id
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByProcInstId(String procInstId);

    /**
     * get sub items by process instance id and added clauses
     *
     * @param procInstId process instance id
     * @param adds added clauses
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByProcInstId(String procInstId, Map<String, Object> adds);

    /**
     * get sub items by process definition key, process instance id and added clauses
     *
     * @param procDefKey process definition key
     * @param procInstId process instance id
     * @param adds added clauses
     * @return sub items
     */
    List<Map<String, Object>> getSubItemsByProcInstId(String procDefKey, String procInstId, Map<String, Object> adds);

    String getMainFormNameByFlowNo(String flowNo);

    String getSubFormNameByFlowNo(String flowNo);

    ProcNode getProcNodeByDefKeyAndNodeKey(String procDefKey, String nodeKey);

    ProcNode getProcNodeByFlowNoAndNodeKey(String flowNo, String nodeKey);

    ProcNode getProcNodeByInstIdAndNodeKey(String procInstId, String nodeKey);

    /**
     * build process full wrapper by process instance id
     *
     * @param procInstId process instance id
     * @return full wrapper
     */
    FullWrapper buildFullByInstId(String procInstId);

    /**
     * build process full wrapper by flow no
     *
     * @param flowNo flow no
     * @return full wrapper
     */
    FullWrapper buildFullFlowNo(String flowNo);
}
