package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.service.ProcQueryColService;
import com.mycx26.base.process.service.bo.QueryListColHeader;
import com.mycx26.base.process.service.bo.QueryQueryCol;
import com.mycx26.base.service.dto.Message;
import com.mycx26.base.service.dto.PageData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程查询列配置 前端控制器
 * </p>
 *
 * @author mycx26
 * @since 2021-01-04
 */
@RestController
@RequestMapping("flow/querycol")
public class ProcQueryColController {

    @Resource
    private ProcQueryColService procQueryColService;

    @RequestMapping("getHeaders")
    public Message<List<QueryListColHeader>> getHeaders(String procDefKey) {
        return Message.success(procQueryColService.getListColHeaders(procDefKey));
    }

    @RequestMapping("getList")
    public Message<PageData<Map<String, Object>>> getList(@RequestParam Map<String, Object> params) {
        return Message.success(procQueryColService.getList(params));
    }

    @RequestMapping("getQueryCols")
    public Message<List<QueryQueryCol>> getQueryCols(String procDefKey) {
        return Message.success(procQueryColService.getQueryCols(procDefKey));
    }

    @RequestMapping("exp")
    public Message<PageData<Map<String, Object>>> exp(@RequestParam Map<String, Object> params) {
        procQueryColService.exp(params);
        return Message.success();
    }
}
