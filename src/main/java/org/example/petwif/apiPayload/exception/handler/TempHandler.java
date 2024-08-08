package org.example.petwif.apiPayload.exception.handler;

import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}