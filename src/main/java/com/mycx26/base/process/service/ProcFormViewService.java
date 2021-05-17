package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcFormView;

/**
 * <p>
 * 流程表单视图 服务类
 * </p>
 *
 * @author mycx26
 * @since 2021-05-17
 */
public interface ProcFormViewService extends IService<ProcFormView> {

    /**
     * get process form view by view key
     *
     * @param viewKey view key
     * @return process form view
     */
    ProcFormView getByViewKey(String viewKey);
}
