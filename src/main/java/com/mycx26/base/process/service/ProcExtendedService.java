package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcInst;

/**
 * @author mycx26
 * @date 2021/9/16
 */
public interface ProcExtendedService {

    /**
     * complete process instance id
     *
     * @param procInst process instance
     */
    void completeProcInstId(ProcInst procInst);

    /**
     * is cancel
     * cancel condition:
     * ①. running process
     * ②. process definition enable cancel
     * ③. all running nodes are enable cancel
     *
     * @param procInst process instance
     * @return true of false
     */
    boolean isCancel(ProcInst procInst);
}
