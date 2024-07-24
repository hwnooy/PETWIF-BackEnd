package org.example.petwif.apiPayload.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.petwif.apiPayload.code.BaseCode;
import org.example.petwif.apiPayload.code.ReasonDto;
import org.example.petwif.apiPayload.code.status.ErrorStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public GeneralException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ReasonDto getErrorStatus() {
        return this.errorStatus.getReason();
    }
    private BaseCode code;

    public ReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }

}