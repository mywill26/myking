package com.mycx26.base.process.service.resolver;

import com.mycx26.base.process.service.bo.ApproveView;

/**
 * @author mycx26
 * @date 2022/8/9
 */
@FunctionalInterface
public interface CombineViewResolver {

    /**
     * resolve combine view structure
     *
     * @param approveView approve view wrapper
     */
    void resolve(ApproveView approveView);
}
