package com.mycx26.base.process.service.resolver;

import com.mycx26.base.process.service.bo.ApproveView;

/**
 * Sub form post resolver for process view columns
 * include approve view & detail view.
 *
 * @author mycx26
 * @date 2021/5/17
 */
@FunctionalInterface
public interface ProcViewSubResolver {

    /**
     * resolve sub form data
     *
     * @param approveView approve view wrapper
     */
    void resolve(ApproveView approveView);
}
