package com.mycx26.base.process.service.handler;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

/**
 * @author mycx26
 * @date 2025/5/19
 */
public interface ProcLifeHandler {

    default void beforeCreate(ProcParamWrapper procParamWrapper) {
    }
}
