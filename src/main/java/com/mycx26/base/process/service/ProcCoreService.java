package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;

import java.util.Map;

/**
 * Process flow core service to join & dispatch.
 *
 * Created by mycx26 on 2020/6/21.
 */
public interface ProcCoreService {

    // Create process general processing and return flow no.
    String create(ProcParamWrapper procParamWrapper);

    // Start process by process engine and return process instance id.
    String start(ProcParamWrapper procParamWrapper);

    void approve(ApproveWrapper approveWrapper);

    Map<String, Object> doApprove(ApproveWrapper approveWrapper);

    void rejectPrevious(ApproveWrapper approveWrapper);

    void rejectFirst(ApproveWrapper approveWrapper);
}
