package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mycx26 on 2020/3/2.
 */
@Getter
@AllArgsConstructor
public enum ProcFormType {

    MAIN("main", "主表"),
    SUB("sub", "子表");

    private String code;

    private String name;
}
