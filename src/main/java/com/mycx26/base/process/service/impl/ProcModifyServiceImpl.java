package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcModifyService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.SqlUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mycx26 on 2020/9/28.
 */
@Service
public class ProcModifyServiceImpl implements ProcModifyService {

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private JdbcService jdbcService;

    @Override
    public void completeProcInstId(ProcInst procInst) {
        procInstService.modifyInstByFlowNo(procInst.getProcInstId(), procInst.getFlowNo());   // complete process instance id

        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
        Map<String, Object> updates = new HashMap<>(1);
        updates.put(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInst.getProcInstId());

        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), procInst.getFlowNo());

        jdbcService.update(procDef.getMainForm(), updates, clauses);
        jdbcService.update(procDef.getSubForm(), updates, clauses);
    }
}
