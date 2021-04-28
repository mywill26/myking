package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mycx26 on 2020/6/23.
 */
@Getter
@AllArgsConstructor
public enum AssigneeType {

    ROLE("1", "内部角色"),
    SUPERIOR("2", "申请人上级"),
    CREATOR("4", "申请人"),
    CUSTOM("5", "自定义");

    private String code;

    private String name;

    public static AssigneeType getByCode(String code) {
        return Stream.of(AssigneeType.values()).filter(e -> e.getCode().equals(code)).collect(Collectors.toList()).get(0);
    }
}
