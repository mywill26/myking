package com.mycx26.base.process.web.controller;

import com.mycx26.base.process.service.ProcFlowNoService;
import com.mycx26.base.service.dto.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 表单流水号生成
 *
 * @author xiayanghui
 * @date 2020/9/2 13:28
 */
@RequestMapping("fng")
@RestController
public class ProcFlowNoController {

    @Resource
    private ProcFlowNoService procFlowNoService;

    @RequestMapping("getFlowNo")
    public Message<String> getFlowNo(String prefix) {
        return Message.success(procFlowNoService.getFlowNo(prefix));
    }
}
