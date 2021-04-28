package com.mycx26.base.process.service.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by mycx26 on 2020/3/8.
 */
@Accessors(chain = true)
@Data
public class ApproveViewQuery {

    private String procInstId;

    private String nodeKey;
}
