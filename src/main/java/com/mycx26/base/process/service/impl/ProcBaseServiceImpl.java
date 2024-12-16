package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcEngineService;
import com.mycx26.base.process.service.ProcFormService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcLockService;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * Default implementation of process base service.
 * <p>
 * Created by mycx26 on 2020/9/18.
 */
@Slf4j
@Service("procBaseService")
public class ProcBaseServiceImpl extends ProcBaseService {

    @Resource
    protected ProcInstService procInstService;

    @Resource
    protected ProcDefService procDefService;

    @Resource
    protected ProcFormService procFormService;

    @Resource
    protected ProcQueryService procQueryService;

    @Resource
    protected ProcLockService procLockService;

    @Resource
    protected ProcEngineService procEngineService;

    @Resource
    protected ProcNodeService procNodeService;

    @Override
    public Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public void afterCreate(ProcParamWrapper procParamWrapper) {
    }

    @Override
    public void endPostHandle(String procInstId) {
        ProcInst procInst = procInstService.end(procInstId);
        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());

        if (procDef.getLockResource()) {
            procLockService.unlockByFlowNo(procInst.getFlowNo());
        }
    }

    @Override
    public void afterEnd(String procInstId) {
    }

    @Override
    public void rejectFirstHandle(String procInstId) {
        ProcInst procInst = procInstService.rejectFirst(procInstId);
        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());

        if (procDef.getLockResource()) {
            procLockService.unlockByFlowNo(procInst.getFlowNo());
        }
    }

    @Override
    public void afterRejectFirst(String procInstId) {
    }

    @Override
    public void cancelHandle(String procInstId) {
        ProcInst procInst = procQueryService.getProcInstByInstId(procInstId);

        procInstService.cancel(procInstId);
        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        if (procDef.getLockResource()) {
            procLockService.unlockByFlowNo(procInst.getFlowNo());
        }
    }

    @Override
    public void afterCancel(String procInstId) {
    }
}
