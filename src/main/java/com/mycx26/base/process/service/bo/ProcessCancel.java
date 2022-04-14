package com.mycx26.base.process.service.bo;

import lombok.Data;

/**
 * Created by mycx26 on 2020/2/29.
 */
@Data
public class ProcessCancel {

    private String procInstId;

    private String creatorId;

    private String comment;
}
