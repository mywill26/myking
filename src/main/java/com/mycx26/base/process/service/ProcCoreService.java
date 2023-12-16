package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.process.service.bo.ReassignWrapper;

/**
 * Process flow core service to join & dispatch.
 *
 * Created by mycx26 on 2020/6/21.
 */
public interface ProcCoreService {

    /**
     * create process uniform handle
     * Is start process depends on procParamWrapper's procInstStatusCode,
     * {@link com.mycx26.base.process.enump.InstanceStatus} Run do start.
     *
     * @param procParamWrapper create parameter wrapper
     * @return process instance id, return null if not start
     */
    String create(ProcParamWrapper procParamWrapper);

    /**
     * start process
     *
     * @param procParamWrapper create parameter wrapper
     * @return process instance id
     */
    String start(ProcParamWrapper procParamWrapper);

    /**
     * approve process
     *
     * @param approveWrapper approve wrapper
     */
    void approve(ApproveWrapper approveWrapper);

    /**
     * reject process to previous nodes
     *
     * @param approveWrapper approve wrapper
     */
    void rejectPrevious(ApproveWrapper approveWrapper);

    /**
     * reject process to first node
     *
     * @param approveWrapper approve wrapper
     */
    void rejectFirst(ApproveWrapper approveWrapper);

    /**
     * cancel process
     *
     * @param approveWrapper approve wrapper
     */
    void cancel(ApproveWrapper approveWrapper);

    /**
     *  reassign task
     *
     * @param reassignWrapper reassign wrapper
     */
    void reassign(ReassignWrapper reassignWrapper);
}
