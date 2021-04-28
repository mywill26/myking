package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.service.ProcNodeHandler;
import com.mycx26.base.process.service.bo.ApproveWrapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mycx26 on 2020/9/18.
 */
@Component
public class DefaultNodeHandler extends ProcNodeHandler {

    @Override
    public void approveValidate(ApproveWrapper approveWrapper) {
    }

    @Override
    public void handleMainForm(ApproveWrapper approveWrapper) {
    }

    @Override
    public void handleSubForm(ApproveWrapper approveWrapper) {
    }

    @Override
    public void handleBizForm(ApproveWrapper approveWrapper) {
    }

    @Override
    public Map<String, Object> handleVars(ApproveWrapper approveWrapper) {
        return new HashMap<>();
    }

    @Override
    public void rejectPreviousHandle(ApproveWrapper approveWrapper) {
    }

    @Override
    public void rejectFirstHandle(ApproveWrapper approveWrapper) {
        ProcInst procInst = procInstService.getByProcInstId(approveWrapper.getProcInstId());
        boolean flag = procInstService.update(Wrappers.<ProcInst>lambdaUpdate()
                .set(ProcInst::getStatusCode, InstanceStatus.REJECT_FIRST.getCode())
                .eq(ProcInst::getProcInstId, approveWrapper.getProcInstId())
                .eq(ProcInst::getStatusCode, procInst.getStatusCode())
        );
        if (!flag) {
            throw new DataException("Process is being operated, please refresh the to do list");
        }

        super.doUnlock(approveWrapper);
    }
}
