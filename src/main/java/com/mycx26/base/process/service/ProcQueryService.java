package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.bo.FullWrapper;

import java.util.List;
import java.util.Map;

/**
 * a polymeric process common query service
 *
 * Created by mycx26 on 2020/6/15.
 */
public interface ProcQueryService {

    /**
     * get process definition by process definition key
     *
     * @param procDefKey process definition key
     * @return process definition
     */
    ProcDef getProcDefByDefKey(String procDefKey);

    /**
     * get process definition by process instance id
     *
     * @param procInstId process instance id
     * @return process definition
     */
    ProcDef getProcDefByInstId(String procInstId);

    /**
     * get process definition by flow no
     *
     * @param flowNo flow no
     * @return process definition
     */
    ProcDef getProcDefByFlowNo(String flowNo);

    /**
     * get process node's view key by process instance id and node key
     *
     * @param procInstId process instance id
     * @param nodeKey node key
     * @return node view key
     */
    String getViewKeyByProcInstIdAndNodeKey(String procInstId, String nodeKey);

    /**
     * get process definition's detail view key by process instance id
     *
     * @param procInstId process instance id
     * @return detail view key
     */
    String getDetailViewKeyByProcInstId(String procInstId);

    /**
     * get process definition's detail view key by flow no
     *
     * @param flowNo flow no
     * @return detail view key
     */
    String getDetailViewKeyByFlowNo(String flowNo);

    /**
     * get process instance by flow no
     *
     * @param flowNo flow no
     * @return process instance
     */
    ProcInst getProcInstByFlowNo(String flowNo);

    /**
     * get process instance by process instance id
     *
     * @param procInstId process instance id
     * @return process instance
     */
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

    /**
     * get process node by process definition key and node key
     *
     * @param procDefKey process definition key
     * @param nodeKey node key
     * @return process node
     */
    ProcNode getProcNodeByDefKeyAndNodeKey(String procDefKey, String nodeKey);

    /**
     * get process node by flow no and node key
     *
     * @param flowNo flow no
     * @param nodeKey node key
     * @return process node
     */
    ProcNode getProcNodeByFlowNoAndNodeKey(String flowNo, String nodeKey);

    /**
     * get process node by process instance id and node key
     *
     * @param procInstId process instance id
     * @param nodeKey node key
     * @return process node
     */
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
