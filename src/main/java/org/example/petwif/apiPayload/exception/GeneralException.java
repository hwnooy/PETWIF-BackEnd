package org.example.petwif.apiPayload.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.code.ErrorReasonDTO;


@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}