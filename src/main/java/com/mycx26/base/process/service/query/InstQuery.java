package com.mycx26.base.process.service.query;

import com.mycx26.base.service.query.base.PageQuery;
import lombok.Data;

/**
 * Created by mycx26 on 2020/3/20.
 */
@Data
public class InstQuery extends PageQuery {

    private String procDefKey;

    private String procInstId;

    private String procInstName;

    private String flowNo;

    private String statusCode;

    private String creatorId;
}
