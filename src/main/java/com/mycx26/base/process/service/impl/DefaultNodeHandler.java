package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.service.ProcNodeHandler;
import com.mycx26.base.process.service.bo.ApproveWrapper;

import java.util.Collections;
import java.util.Map;

/**
 * Created by mycx26 on 2020/9/18.
 */
public abstract class DefaultNodeHandler extends ProcNodeHandler {

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
        return Collections.emptyMap();
    }

    @Override
    public void rejectPreviousHandle(ApproveWrapper approveWrapper) {
    }

    @Override
    public void rejectFirstHandle(ApproveWrapper approveWrapper) {
    }
}
