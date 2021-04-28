package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcRole;
import com.mycx26.base.process.service.query.ProcRoleQuery;
import com.mycx26.base.service.dto.PageData;

/**
 * <p>
 * 流程角色 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-28
 */
public interface ProcRoleService extends IService<ProcRole> {

    PageData<ProcRole> getList(ProcRoleQuery roleQuery);

    void modify(ProcRole procRole);

    ProcRole getByRoleCode(String roleCode);

    String getRoleNameByCode(String roleCode);
}
