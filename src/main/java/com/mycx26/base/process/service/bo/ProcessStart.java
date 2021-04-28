package com.mycx26.base.process.service.bo;

import lombok.Data;

import java.util.Map;

/**
 * Created by mycx26 on 2020/2/29.
 */
@Data
public class ProcessStart {

    private String procDefKey;

    private String procInstId;

    private String businessKey;

    private String name;

    private String creatorIdKey;    // ignore

    private String creatorId;

    private Map<String, Object> vars;

    /**
     * Is skip first node when start.
     */
    private Boolean isSkipFirst;    // ignore
}
