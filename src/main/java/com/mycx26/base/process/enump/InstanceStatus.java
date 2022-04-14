package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mycx26 on 2020/3/2.
 */
@Getter
@AllArgsConstructor
public enum InstanceStatus {

    /**
     * 草稿
     */
    DRAFT("-0", "草稿"),
    /**
     * 审批中
     */
    RUN("0", "审批中"),
    /**
     * 已完成
     */
    END("1", "已完成"),
    /**
     * 待启动
     */
    TO_START("2", "待启动"),
    /**
     * 驳回申请
     */
    REJECT_FIRST("3", "驳回申请"),
    /**
     * 驳回上级
     */
    REJECT_PREVIOUS("4", "驳回上级"),
    /**
     * 已取消
     */
    CANCELED("5", "已取消");

    private String code;

    private String name;
}
