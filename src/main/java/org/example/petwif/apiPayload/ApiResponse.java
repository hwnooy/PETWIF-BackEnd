package org.example.petwif.apiPayload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.petwif.apiPayload.code.BaseCode;
import org.example.petwif.apiPayload.code.status.SuccessStatus;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {


    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private T data;

    // 성공한 경우 응답 생성
    public static <T> ApiResponse<T> onSuccess(T data){
        return new ApiResponse<>(true, SuccessStatus.OK.getCode(), SuccessStatus.OK.getMessage(), data);
    }

// 임의로 추가
    public static <T> ApiResponse<T> of(BaseCode code) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), null);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }


    //실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message, T data ){
        return new ApiResponse<>(false, code, message, data);
    }
}

