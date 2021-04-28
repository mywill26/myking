package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mycx26 on 2020/3/2.
 */
@Getter
@AllArgsConstructor
public enum ProcViewColType {

    STR("string", "字符串"),
    ENUM("enum", "枚举"),
    E_ENUM("e-enum", "外部枚举");

    private String code;

    private String name;
}
