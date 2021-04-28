package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mycx26 on 2020/3/2.
 */
@Getter
@AllArgsConstructor
public enum InstanceStatus {

    DRAFT("-0", "草稿"),
    RUN("0", "审批中"),
    END("1", "已完成"),
    TO_START("2", "待启动"),
    REJECT_FIRST("3", "驳回申请"),
    REJECT_PREVIOUS("4", "驳回上级"),
    CANCELED("5", "已取消");

    private String code;

    private String name;
}
