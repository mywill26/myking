package com.mycx26.base.process.service.handler;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

/**
 * Process create pre-handler for handle sth before form data handle.
 *
 * @author mycx26
 * @date 2021/6/25
 */
@FunctionalInterface
public interface ProcCreatePreHandler {

    /**
     * process create pre-handle
     *
     * @param procParamWrapper process param wrapper
     */
    void createPreHandle(ProcParamWrapper procParamWrapper);
}
