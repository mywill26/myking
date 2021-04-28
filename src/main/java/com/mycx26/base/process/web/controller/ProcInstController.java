package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.query.InstQuery;
import com.mycx26.base.service.dto.Message;
import com.mycx26.base.service.dto.PageData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 流程实例 前端控制器
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@RestController
@RequestMapping("${proc-prefix}/inst")
public class ProcInstController {

    @Resource
    private ProcInstService procInstService;

    @RequestMapping("getList")
    public Message<PageData<ProcInst>> getList(InstQuery instQuery) {
        return Message.success(procInstService.getList(instQuery));
    }

}
