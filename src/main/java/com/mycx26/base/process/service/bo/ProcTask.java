package com.mycx26.base.process.service.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by mycx26 on 2020/2/29.
 */
@Accessors(chain = true)
@Data
public class ProcTask {

    private String procDefId;   // ignore

    private String procDefKey;

    private String procDefName;

    private String procInstId;

    private String curTaskId;

    private String curTaskKey;

    private String curTaskName;

    private String assignees;
}
