package org.example.petwif.apiPayload.exception.handler;

import org.example.petwif.apiPayload.code.BaseCode;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseCode errorCode) {
        super((ErrorStatus) errorCode);
    }
}