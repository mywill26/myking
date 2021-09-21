package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcLock;
import com.mycx26.base.service.base.BaseService;

import java.util.List;

/**
 * <p>
 * 流程资源锁定 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-09-16
 */
public interface ProcLockService extends BaseService<ProcLock> {

    void lock(List<ProcLock> procLocks);

    void unlock(List<String> resourceIds);

    /**
     * unlock resource by flow no
     *
     * @param flowNo flow no
     */
    void unlockByFlowNo(String flowNo);

    // get resource lock state related process
    List<ProcLock> getByResourceIds(List<String> resourceIds);

    // try lock when problem exist
    void lockByFlowNos(List<String> flowNos);
}
