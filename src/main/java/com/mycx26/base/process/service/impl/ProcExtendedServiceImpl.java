package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.service.ProcEngineService;
import com.mycx26.base.process.service.ProcExtendedService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.process.service.bo.ProcTask;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SqlUtil;
import com.mycx26.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mycx26
 * @date 2021/9/16
 */
@Slf4j
@Service
public class ProcExtendedServiceImpl implements ProcExtendedService {

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcQueryService procQueryService;

    @Resource
    private JdbcService jdbcService;

    @Resource
    private ProcEngineService procEngineService;

    @Resource
    private ProcNodeService procNodeService;

    @Override
    public void completeProcInstId(ProcInst procInst) {
        // complete process instance id
        procInstService.update(Wrappers.<ProcInst>lambdaUpdate()
                .set(ProcInst::getProcInstId, procInst.getProcInstId())
                .set(ProcInst::getStatusCode, InstanceStatus.RUN.getCode())
                .eq(ProcInst::getFlowNo, procInst.getFlowNo()));

        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        Map<String, Object> updates = new HashMap<>(1);
        updates.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInst.getProcInstId());

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), procInst.getFlowNo());

        jdbcService.update(procDef.getMainForm(), updates, clauses);
        if (StringUtil.isNotBlank(procDef.getSubForm())) {
            jdbcService.update(procDef.getSubForm(), updates, clauses);
        }
    }

    @Override
    public boolean isCancel(ProcInst procInst) {
        String procInstId = procInst.getProcInstId();
        if (!InstanceStatus.RUN.getCode().equals(procInst.getProcInstStatus())) {
            return false;
        }

        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        if (null == procDef || !procDef.getCancel()) {
            return false;
        }

        List<ProcTask> tasks = procEngineService.getRunningTasks(procInst.getProcInstId());
        if (CollectionUtil.isEmpty(tasks)) {
            log.error("Process engine running tasks data error: [{}]", procInstId);
            return false;
        }
        List<String> nodeKeys = tasks.stream().map(ProcTask::getCurTaskKey).collect(Collectors.toList());
        List<ProcNode> procNodes = procNodeService.getByProcDefKeyAndNodeKeys(procDef.getProcDefKey(), nodeKeys);
        return procNodes.stream().filter(e -> e.getCancel() != null && e.getCancel()).count() == procNodes.size();
    }
}
