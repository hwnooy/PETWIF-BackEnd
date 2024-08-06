package org.example.petwif.apiPayload.exception.handler;

import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.exception.GeneralException;

public class BlockHandler extends GeneralException {
    public BlockHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}