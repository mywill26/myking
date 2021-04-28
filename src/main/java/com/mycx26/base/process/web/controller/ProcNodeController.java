package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.query.NodeQuery;
import com.mycx26.base.service.dto.Message;
import com.mycx26.base.service.dto.PageData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 流程节点 前端控制器
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@RestController
@RequestMapping("${proc-prefix}/node")
public class ProcNodeController {

    @Resource
    private ProcNodeService procNodeService;

    @RequestMapping("getList")
    public Message<PageData<ProcNode>> getList(NodeQuery nodeQuery) {
        return Message.success(procNodeService.getList(nodeQuery));
    }

    @PostMapping("init")
    public Message init(String procDefKey) {
        procNodeService.init(procDefKey);
        return Message.success();
    }

    @PostMapping("add")
    public Message add(ProcNode procNode) {
        procNodeService.add(procNode);
        return Message.success();
    }

    @PostMapping("modify")
    public Message modify(ProcNode procNode) {
        procNodeService.modify(procNode);
        return Message.success();
    }

    @GetMapping("getFirstNode/{procDefKey}")
    public Message<ProcNode> getFirstNode(@PathVariable String procDefKey) {
        return Message.success(procNodeService.getFirst(procDefKey));
    }
}
