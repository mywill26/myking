package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.entity.ProcRole;
import com.mycx26.base.process.service.ProcRoleService;
import com.mycx26.base.process.service.query.ProcRoleQuery;
import com.mycx26.base.service.dto.Message;
import com.mycx26.base.service.dto.PageData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 流程角色 前端控制器
 * </p>
 *
 * @author mycx26
 * @since 2020-06-28
 */
@RestController
@RequestMapping("flow/role")
public class ProcRoleController {

    @Resource
    private ProcRoleService procRoleService;

    @RequestMapping("getList")
    public Message<PageData<ProcRole>> getList(ProcRoleQuery roleQuery) {
        return Message.success(procRoleService.getList(roleQuery));
    }

    @PostMapping("modify")
    public Message modify(ProcRole procRole) {
        procRoleService.modify(procRole);
        return Message.success();
    }
}
