package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcExtendedService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.SqlUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mycx26
 * @date 2021/9/16
 */
@Service
public class ProcExtendedServiceImpl implements ProcExtendedService {

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private JdbcService jdbcService;

    @Override
    public void completeProcInstId(ProcInst procInst) {
        // complete process instance id
        procInstService.update(Wrappers.<ProcInst>lambdaUpdate()
                .set(ProcInst::getProcInstId, procInst.getProcInstId())
                .set(ProcInst::getStatusCode, InstanceStatus.RUN.getCode())
                .eq(ProcInst::getFlowNo, procInst.getFlowNo()));

        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
        Map<String, Object> updates = new HashMap<>(1);
        updates.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInst.getProcInstId());

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), procInst.getFlowNo());

        jdbcService.update(procDef.getMainForm(), updates, clauses);
        jdbcService.update(procDef.getSubForm(), updates, clauses);
    }
}
