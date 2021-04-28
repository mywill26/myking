package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.query.NodeQuery;
import com.mycx26.base.service.dto.PageData;

import java.util.List;

/**
 * <p>
 * 流程节点 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
public interface ProcNodeService extends IService<ProcNode> {

    PageData<ProcNode> getList(NodeQuery nodeQuery);

    void init(String procDefKey);

    void add(ProcNode procNode);

    void modify(ProcNode procNode);

    ProcNode getFirst(String procDefKey);

    List<ProcNode> getByProcDefKeyAndNodeKeys(String procDefKey, List<String> nodeKeys);

    ProcNode getByProcDefKeyAndNodeKey(String procDefKey, String nodeKey);

    String getViewKeyByProcDefKeyAndNodeKey(String procDefKey, String nodeKey);

    List<ProcNode> getByCategoryCode(String categoryCode);
}
