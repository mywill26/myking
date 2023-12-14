package com.mycx26.base.process.service.resolver;

import com.mycx26.base.process.service.bo.ApproveView;

/**
 * @author mycx26
 * @date 2023/12/14
 */
public interface LightViewResolver {

    /**
     * resolve light view
     *
     * @param approveView approve view wrapper
     */
    void resolve(ApproveView approveView);
}
