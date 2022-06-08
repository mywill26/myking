package com.mycx26.base.process.service.bo;

import lombok.Data;

/**
 * @author mycx26
 * @date 2022/6/8
 */
@Data
public class TaskReassign {

    private String taskId;

    private String userId;

    private String comment;

    /**
     * comma separated if multiple
     */
    private String toUserId;

    /**
     * 引擎key
     */
    private String engineKey;
}
