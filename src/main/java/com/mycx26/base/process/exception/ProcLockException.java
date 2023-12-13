package com.mycx26.base.process.exception;

import com.mycx26.base.exception.DataException;

/**
 * @author mycx26
 * @date 2023/12/12
 */
public class ProcLockException extends DataException {

    private static final long serialVersionUID = 1L;

    public ProcLockException(String message) {
        super(message);
    }

    public ProcLockException(String code, Object[] args) {
        super(code, args);
    }
}
