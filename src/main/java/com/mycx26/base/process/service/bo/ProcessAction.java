package com.mycx26.base.process.service.bo;

import lombok.Data;

import java.util.Map;

/**
 * Created by mycx26 on 2021/4/13.
 */
@Data
public class ProcessAction {

    private String procInstId;

    private String userId;

    private String taskId;

    private String comment;

    private Map<String, Object> vars;
}
