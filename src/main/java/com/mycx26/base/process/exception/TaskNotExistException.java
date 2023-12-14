package com.mycx26.base.process.exception;

import com.mycx26.base.exception.base.AppException;

/**
 * @author mycx26
 * @date 2023/12/14
 */
public class TaskNotExistException extends AppException {

    private static final long serialVersionUID = 1L;

    public TaskNotExistException(String message) {
        super(message);
    }

    public TaskNotExistException(String code, Object[] args) {
        super(code, args);
    }
}
