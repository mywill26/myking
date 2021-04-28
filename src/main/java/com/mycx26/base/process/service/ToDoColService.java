package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ToDoCol;
import com.mycx26.base.service.base.BaseService;

import java.util.List;

/**
 * <p>
 * 待办列配置 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-07-13
 */
public interface ToDoColService extends BaseService<ToDoCol> {

    List<ToDoCol> getByProcAndFormType(String procDefKey, String formTypeCode);
}
