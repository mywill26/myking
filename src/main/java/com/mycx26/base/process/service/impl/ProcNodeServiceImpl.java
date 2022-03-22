package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.constant.CacheConstant;
import com.mycx26.base.context.UserContext;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.mapper.ProcNodeMapper;
import com.mycx26.base.process.service.ProcEngineService;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.query.NodeQuery;
import com.mycx26.base.service.dto.PageData;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 流程节点 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Service
public class ProcNodeServiceImpl extends ServiceImpl<ProcNodeMapper, ProcNode> implements ProcNodeService {

    @Resource
    private ProcEngineService procEngineService;

    @Resource
    private ProcNodeMapper procNodeMapper;

    private ProcNodeService procNodeService;

    @PostConstruct
    private void init() {
        procNodeService = SpringUtil.getBean(ProcNodeServiceImpl.class);
    }

    @Override
    public PageData<ProcNode> getList(NodeQuery nodeQuery) {
        return new PageData<>(page(new Page<>(nodeQuery.getCurrent(), nodeQuery.getSize()),
                new QueryWrapper<ProcNode>()
                        .like(StringUtil.isNotBlank(nodeQuery.getNodeKey()), "node_key", nodeQuery.getNodeKey())
                        .like(StringUtil.isNotBlank(nodeQuery.getNodeName()), "node_name", nodeQuery.getNodeName())
                        .eq(StringUtil.isNotBlank(nodeQuery.getProcDefKey()), "proc_def_key", nodeQuery.getProcDefKey())
                        .orderByDesc("create_time")
        ));
    }

    @Override
    public void init(String procDefKey) {
        if (StringUtil.isBlank(procDefKey)) {
            throw new ParamException("Process definition key is required");
        }

        List<ProcNode> nodes = procEngineService.getNodes(procDefKey);
        if (nodes.isEmpty()) {
            return;
        }

        nodes.forEach(e -> e.setCreatorId(UserContext.getUserId()).setModifierId(UserContext.getUserId()).setProcDefKey(procDefKey));
        procNodeMapper.init(nodes);
    }

    @Override
    public void add(ProcNode procNode) {
        addValidate(procNode);

        procNode.setCreatorId(UserContext.getUserId());

        try {
            save(procNode);
        } catch (DuplicateKeyException e) {
            throw new DataException("Data is exist");
        }
    }

    private void addValidate(ProcNode procNode) {
        StringBuilder sb = new StringBuilder();

        if (StringUtil.isBlank(procNode.getProcDefKey())) {
            StringUtil.append(sb, "Process definition key is required");
        }
        if (StringUtil.isBlank(procNode.getNodeKey())) {
            StringUtil.append(sb, "Node key is required");
        }
        if (StringUtil.isBlank(procNode.getNodeName())) {
            StringUtil.append(sb, "Node name is required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    @Override
    public void modify(ProcNode procNode) {
        if (StringUtil.isAnyBlank(procNode.getProcDefKey(), procNode.getNodeKey())) {
            throw new ParamException("Process definition key, node key are required");
        }

        if (StringUtil.isNotBlank(procNode.getTips())) {
            procNode.setTips(StringEscapeUtils.unescapeHtml4(procNode.getTips()));
        }
        update(Wrappers.<ProcNode>lambdaUpdate()
                .set(StringUtil.isNotBlank(procNode.getNodeName()), ProcNode::getNodeName, procNode.getNodeName())
//                .set(ProcNode::getNodeHandler, procNode.getNodeHandler())
//                .set(ProcNode::getViewKey, procNode.getViewKey())
                .set(ProcNode::getRejectPrevious, procNode.getRejectPrevious())
                .set(ProcNode::getRejectFirst, procNode.getRejectFirst())
                .set(ProcNode::getFirst, procNode.getFirst())
                .set(ProcNode::getTips, procNode.getTips())
                .set(ProcNode::getYn, procNode.getYn())
                .set(ProcNode::getModifierId, UserContext.getUserId())
                .eq(ProcNode::getProcDefKey, procNode.getProcDefKey()).eq(ProcNode::getNodeKey, procNode.getNodeKey())
        );

        procNodeService.deleteCacheByProcDefKeyAndNodeKey(procNode.getProcDefKey(), procNode.getNodeKey());
        procNodeService.deleteFirstCacheByProcDefKey(procNode.getProcDefKey());
    }

    @Cacheable(value = ProcCacheConstant.NODE_FIRST, cacheManager = CacheConstant.CENTER_MANAGER,
            key="#procDefKey", unless = "null == #result")
    @Override
    public ProcNode getFirst(String procDefKey) {
        if (StringUtil.isBlank(procDefKey)) {
            return null;
        }

        List<ProcNode> nodes = list(new QueryWrapper<ProcNode>()
                .eq("proc_def_key", procDefKey)
                .eq("is_first", Yn.YES.getCode())
                .eq("yn", Yn.YES.getCode())
        );

        if (nodes.size() > 1) {
            throw new DataException("Multiple first node config exist");
        }

        return nodes.get(0);
    }

    @CacheEvict(value = ProcCacheConstant.NODE_FIRST, cacheManager = CacheConstant.CENTER_MANAGER, key="#procDefKey")
    @Override
    public void deleteFirstCacheByProcDefKey(String procDefKey) {
        assert StringUtil.isNotBlank(procDefKey);
    }

    @Override
    public List<ProcNode> getByProcDefKeyAndNodeKeys(String procDefKey, List<String> nodeKeys) {
        if (StringUtil.isBlank(procDefKey) || CollectionUtil.isEmpty(nodeKeys)) {
            return Collections.emptyList();
        }

        return list(new QueryWrapper<ProcNode>().eq("proc_def_key", procDefKey).in("node_key", nodeKeys).eq("yn", Yn.YES.getCode()));
    }

    @Cacheable(value = ProcCacheConstant.PROC_NODE, cacheManager = CacheConstant.CENTER_MANAGER,
            key="#procDefKey + '&' + #nodeKey", unless = "null == #result")
    @Override
    public ProcNode getByProcDefKeyAndNodeKey(String procDefKey, String nodeKey) {
        if (StringUtil.isAnyBlank(procDefKey, nodeKey)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcNode>().eq("proc_def_key", procDefKey).eq("node_key", nodeKey).eq("yn", Yn.YES.getCode()));
    }

    @CacheEvict(value = ProcCacheConstant.PROC_NODE, cacheManager = CacheConstant.CENTER_MANAGER,
            key="#procDefKey + '&' + #nodeKey")
    @Override
    public void deleteCacheByProcDefKeyAndNodeKey(String procDefKey, String nodeKey) {
        assert StringUtil.isAnyNotBlank(procDefKey, nodeKey);
    }

    @Override
    public String getViewKeyByProcDefKeyAndNodeKey(String procDefKey, String nodeKey) {
        ProcNode procNode = procNodeService.getByProcDefKeyAndNodeKey(procDefKey, nodeKey);

        if (procNode != null) {
            return procNode.getViewKey();
        }

        return null;
    }

    @Cacheable(value = ProcCacheConstant.NODE_CATEGORY, key="#categoryCode",
            unless = "null == #result || #result.isEmpty()")
    @Override
    public List<ProcNode> getByCategoryCode(String categoryCode) {
        if (StringUtil.isBlank(categoryCode)) {
            return Collections.emptyList();
        }

        return procNodeMapper.getByCategoryCode(categoryCode);
    }
}
