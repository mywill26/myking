package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

import java.util.Map;

/**
 * Created by mycx26 on 2020/6/24.
 */
public abstract class ProcBaseService {

    public static final String SUFFIX = "Service";

    // set process start variables and return
    public abstract Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper);

    /**
     * Callback after completion of create process.
     * The execution failure does not affect the main process.
     *
     * @param procParamWrapper create param
     */
    public abstract void afterCreate(ProcParamWrapper procParamWrapper);

    // process end postHandle
    public abstract void endPostHandle(String procInstId);

    /**
     * Called after the process is rejected to first node.
     *
     * @param procInstId process instance id
     */
    public abstract void rejectFirstHandle(String procInstId);
}
