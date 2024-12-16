package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.EngineNoticeService;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcNodeHandler;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.util.ExpAssert;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mycx26
 * @date 2022/7/21
 */
@Service
public class EngineNoticeServiceImpl implements EngineNoticeService {

    @Resource
    private ProcQueryService procQueryService;

    @Resource(name = "procBaseService")
    private ProcBaseService procBaseService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void endNotice(String procInstId) {
        ProcBaseService service = validateBase(procInstId);
        service.endPostHandle(procInstId);

        threadPoolTaskExecutor.execute(() -> service.afterEnd(procInstId));
    }

    private ProcBaseService validateBase(String procInstId) {
        ProcInst procInst = validateProcInst(procInstId);

        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        ProcBaseService service = SpringUtil.getBean2(procDef.getProcDefKey() + ProcBaseService.SUFFIX);
        if (null == service) {
            service = procBaseService;
        }
        return service;
    }

    private ProcInst validateProcInst(String procInstId) {
        ExpAssert.isFalse(StringUtil.isBlank(procInstId), "Process instance id is required");
        ProcInst procInst = procQueryService.getProcInstByInstId(procInstId);
        ExpAssert.isFalse(null == procInst, "Process instance not exist");

        return procInst;
    }

    @Override
    public void rejectFirstNotice(String procInstId) {
        ProcBaseService service = validateBase(procInstId);
        service.rejectFirstHandle(procInstId);

        threadPoolTaskExecutor.execute(() -> service.afterRejectFirst(procInstId));
    }

    @Override
    public void cancelNotice(String procInstId) {
        ProcBaseService service = validateBase(procInstId);
        service.cancelHandle(procInstId);

        threadPoolTaskExecutor.execute(() -> service.afterCancel(procInstId));
    }

    @Override
    public void arriveNodeNotice(String procInstId, String nodeKey) {
        ProcInst procInst = validateProcInst(procInstId);
        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        ProcNode procNode = procQueryService.getProcNodeByDefKeyAndNodeKey(procDef.getProcDefKey(), nodeKey);
        ExpAssert.isFalse(null == procNode, "Process node not exist");

        if (StringUtil.isNotBlank(procNode.getNodeHandler())) {
            ProcNodeHandler nodeHandler = SpringUtil.getBean2(procNode.getNodeHandler());
            assert nodeHandler != null;
            nodeHandler.arrive(procInstId, nodeKey);
        }
    }
}
