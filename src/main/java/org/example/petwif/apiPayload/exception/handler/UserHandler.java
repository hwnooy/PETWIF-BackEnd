package org.example.petwif.apiPayload.exception.handler;

import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}