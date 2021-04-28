package com.mycx26.base.process.service.query;

import com.mycx26.base.service.query.base.PageQuery;
import lombok.Data;

/**
 * Created by mycx26 on 2020/6/30.
 */
@Data
public class ProcRoleQuery extends PageQuery {

    private String roleName;

    private String userId;
}
