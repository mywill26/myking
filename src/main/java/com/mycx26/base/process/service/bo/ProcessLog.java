package com.mycx26.base.process.service.bo;

import com.mycx26.base.process.enump.ProcLogCategory;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * display only
 *
 * Created by mycx26 on 2020/2/29.
 */
@Accessors(chain = true)
@Data
public class ProcessLog {

    private String nodeKey;

    private String nodeName;

    private String userId;

    private String username;

    private String avatarUrl;

    private String operateCode;

    private String operateName;

    /**
     * Approval comment
     */
    private String comment;

    private String startTime;

    private String endTime;

    private String categoryCode = ProcLogCategory.PAST.getCode();
}
