package com.mycx26.base.process.enump;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mycx26 on 2020/6/23.
 */
@Getter
@AllArgsConstructor
public enum ProcLogCategory {

    PAST("0", "历史"),
    PRESENT("1", "现在"),
    FUTURE("2", "将来");

    private String code;

    private String name;
}
