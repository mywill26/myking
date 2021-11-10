package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

import java.util.Map;

/**
 * Created by mycx26 on 2020/6/24.
 */
public abstract class ProcBaseService {

    public static final String SUFFIX = "Service";

    /**
     * set process start variables
     *
     * @param procParamWrapper create parameter wrapper
     * @return variables
     */
    public abstract Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper);

    /**
     * Callback after completion of create process.
     * The execution failure should not affect the main process.
     *
     * @param procParamWrapper create parameter wrapper
     */
    public abstract void afterCreate(ProcParamWrapper procParamWrapper);

    /**
     * handle when process end
     *
     * @param procInstId process instance id
     */
    public abstract void endPostHandle(String procInstId);

    /**
     * Callback after end of process.
     * The execution failure should not affect the main process.
     *
     * @param procInstId process instance id
     */
    public abstract void afterEnd(String procInstId);

    /**
     * handle when process is rejected to first node
     *
     * @param procInstId process instance id
     */
    public abstract void rejectFirstHandle(String procInstId);
}
