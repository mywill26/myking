package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.query.DefQuery;
import com.mycx26.base.service.bo.SelectOption;
import com.mycx26.base.service.dto.Message;
import com.mycx26.base.service.dto.PageData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 流程定义 前端控制器
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@RestController
@RequestMapping("flow/def")
public class ProcDefController {

    @Resource
    private ProcDefService procDefService;

    @RequestMapping("getList")
    public Message<PageData<ProcDef>> getList(DefQuery defQuery) {
        return Message.success(procDefService.getList(defQuery));
    }

    @PostMapping("add")
    public Message add(ProcDef procDef) {
        procDefService.add(procDef);
        return Message.success();
    }

    @PostMapping("modify")
    public Message modify(ProcDef procDef) {
        procDefService.modify(procDef);
        return Message.success();
    }

    @RequestMapping("getAll")
    public Message<List<SelectOption>> getAll() {
        return Message.success(procDefService.getAll());
    }

    @RequestMapping("get")
    public Message<ProcDef> get(String procDefKey) {
        return Message.success(procDefService.getByKey(procDefKey));
    }
}
