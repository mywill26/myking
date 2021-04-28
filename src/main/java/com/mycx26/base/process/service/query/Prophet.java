package com.mycx26.base.process.service.query;

import lombok.Data;

import java.util.Map;

/**
 * Created by mycx26 on 2020/3/20.
 */
@Data
public class Prophet {

    private String procDefKey;

    private String curNodeKey;

    private Map<String, Object> vars;
}
