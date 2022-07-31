package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.EngineNoticeService;
import com.mycx26.base.process.service.ProcBaseService;
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
        ExpAssert.isFalse(StringUtil.isBlank(procInstId), "Process instance id is required");
        ProcInst procInst = procQueryService.getProcInstByInstId(procInstId);
        ExpAssert.isFalse(null == procInst, "Process instance not exist");

        assert procInst != null;
        ProcDef procDef = procQueryService.getProcDefByDefKey(procInst.getProcDefKey());
        ProcBaseService service = SpringUtil.getBean2(procDef.getProcDefKey() + ProcBaseService.SUFFIX);
        if (null == service) {
            service = procBaseService;
        }
        return service;
    }

    @Override
    public void rejectFirstNotice(String procInstId) {
        ProcBaseService service = validateBase(procInstId);
        service.rejectFirstHandle(procInstId);
    }
}
