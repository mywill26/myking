package com.mycx26.base.process.service.query;

import com.mycx26.base.service.query.base.PageQuery;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/3/8.
 */
@Accessors(chain = true)
@Data
public class TaskQuery extends PageQuery {

    private String procDefKey;

    private String userId;

    private Map<String, Object> params;

    private List<String> procDefKeys;
}
