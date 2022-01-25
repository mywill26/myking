package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.base.AppException;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcLock;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.mapper.ProcLockMapper;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcLockService;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.service.base.impl.BaseServiceImpl;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程资源锁定 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-09-16
 */
@Slf4j
@Service
public class ProcLockServiceImpl extends BaseServiceImpl<ProcLockMapper, ProcLock> implements ProcLockService {

    private static final String TBL_NAME = "flow_proc_lock";

    private static final List<String> COLS = Arrays.asList("resource_id", "flow_no");

    @Resource
    private ProcLockMapper procLockMapper;

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcQueryService procQueryService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private JdbcService jdbcService;

    @Override
    public void lock(List<ProcLock> procLocks) {
        if (CollectionUtil.isEmpty(procLocks)) {
            return;
        }
        List<ProcLock> collect = procLocks.stream().filter(e -> StringUtil.isNotBlank(e.getResourceId())).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return;
        }

        try {
            procLockMapper.insertBatchSomeColumn(procLocks);
        } catch (DuplicateKeyException e) {
            throw new DataException("Resource has bean locked");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlock(List<String> resourceIds) {
        if (CollectionUtil.isEmpty(resourceIds)) {
            return;
        }
        List<String> collect = resourceIds.stream().filter(StringUtil::isNotBlank).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return;
        }

        int count = procLockMapper.delete(Wrappers.<ProcLock>lambdaUpdate().in(ProcLock::getResourceId, resourceIds));
        if (count != collect.size()) {
            throw new AppException("Resource unlock fail");
        }
    }

    @Override
    public void unlockByFlowNo(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return;
        }

        remove(Wrappers.<ProcLock>lambdaUpdate().eq(ProcLock::getFlowNo, flowNo));
    }

    @Override
    public List<ProcLock> getByResourceIds(List<String> resourceIds) {
        if (CollectionUtil.isEmpty(resourceIds)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ProcLock>lambdaQuery().in(ProcLock::getResourceId, resourceIds));
    }

    @Override
    public void lockByFlowNos(List<String> flowNos) {
        if (CollectionUtil.isEmpty(flowNos)) {
            return;
        }

        List<ProcInst> procInsts = procInstService.getByFlowNos(flowNos);
        procInsts.forEach(procInst -> {
            ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
            if (!procDef.getLockResource() || StringUtil.isBlank(procDef.getResourceKey())) {
                return;
            }

            if (InstanceStatus.RUN.getCode().equals(procInst.getStatusCode())) {
                List<Map<String, Object>> subItems = procQueryService.getSubItemsByFlowNo(procInst.getFlowNo());
                if (subItems.isEmpty()) {
                    return;
                }
                List<String> resourceIds = subItems.stream()
                        .filter(item -> StringUtil.isNotBlank((String) item.get(procDef.getResourceKey())))
                        .map(item -> (String)item.get(procDef.getResourceKey())).collect(Collectors.toList());
                if (resourceIds.isEmpty()) {
                    return;
                }
                List<List<Object>> objectList = new ArrayList<>(resourceIds.size());
                resourceIds.forEach(e -> {
                    List<Object> objects = new ArrayList<>(2);
                    objects.add(e);
                    objects.add(procInst.getFlowNo());
                    objectList.add(objects);
                });
                jdbcService.insertIgnoreBatch(TBL_NAME, COLS, objectList);
            }
        });
    }
}
