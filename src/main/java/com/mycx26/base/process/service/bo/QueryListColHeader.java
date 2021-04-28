package com.mycx26.base.process.service.bo;

import lombok.Data;

/**
 * Process query list col header info.
 *
 * Created by mycx26 on 2020/7/13.
 */
@Data
public class QueryListColHeader {

    private String propName;

    private String colName;

    private Boolean display;
}
