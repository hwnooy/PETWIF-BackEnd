package org.example.petwif.apiPayload.code.status;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.petwif.apiPayload.code.BaseErrorCode;
import org.example.petwif.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 로그인 관련
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "user already exists", "이미 존재하는 아이디입니다."),
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "LOGIN FAIL", "아이디 또는 비밀번호를 확인하세요"),

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),

    // Notification Error
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "Notification4001", "알람을 찾을 수 없습니다."),
    NOTIFICATION_DTYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Notification4002", "유효하지 않은 데이터 타입입니다."),
    NOTIFICATION_PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Notification4002", "알람 리스트를 확인할 수 없습니다. 유효하지 않은 페이지입니다."),

    // Comment Error
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4001", "댓글이 없습니다."),

    // Follow Error
    FRIEND_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "FRIEND4001", "이미 친구입니다."),
    FRIEND_NOT_FOUND(HttpStatus.BAD_REQUEST, "FRIEND4002", "친구가 아닙니다."),
    FRIEND_REQUEST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "FRIEND4003", "이미 친구 요청한 상태입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "FRIEND4004", "친구 요청이 존재하지 않습니다."),
    FRIEND_REJECT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "FRIEND4005", "이미 친구 요청을 거절한 상태입니다."),
    FRIEND_PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "FRIEND4006", "친구 리스트를 확인할 수 없습니다. 유효하지 않은 페이지입니다."),

    // Album Error
    ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4001", "앨범을 찾을 수 없습니다."),
    ALBUM_UNAUTHORIZED(HttpStatus.FORBIDDEN, "ALBUM4020", "앨범에 대한 권한이 없습니다."),
    ALBUM_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4021", "앨범 리스트를 찾을 수 없습니다."),
    ALBUM_ACCESS_RESTRICTED(HttpStatus.FORBIDDEN, "ALBUM4002", "앨범 보기(열람) 권한이 없습니다."),
    ALBUM_SCOPE_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4003", "앨범 공개 범위를 확인할 수 없습니다."),

    // Album Image Error
    ALBUM_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"ALBUM4004", "앨범 내의 이미지를 찾을 수 없습니다."),
    ALBUM_IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST,"ALBUM4005", "앨범의 이미지 업로드를 실패했습니다."),

    // AlbumLike Error
    ALBUM_LIKE_EXIST(HttpStatus.CONFLICT, "ALBUM4006", "이미 앨범 좋아요가 있습니다."),
    ALBUM_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4007", "앨범 좋아요가 존재하지 않습니다."),
    ALBUM_LIKE_PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4008", "앨범 좋아요 리스트를 찾을 수 없습니다."),

    // AlbumBookmark Error
    ALBUM_BOOKMARK_EXIST(HttpStatus.CONFLICT, "ALBUM4009", "이미 앨범 북마크가 있습니다."),
    ALBUM_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4010", "앨범 북마크가 존재하지 않습니다."),
    ALBUM_BOOKMARK_PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "ALBUM4011", "앨범 북마크 리스트를 찾을 수 없습니다."),

    // AlbumReport Error
    ALBUM_REPORT_EXIST(HttpStatus.CONFLICT, "ALBUM4012", "이미 신고를 했습니다."),


    BLOCK_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "BLOCK4001", "이미 차단된 사용자입니다."),
    BLOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "BLOCK4002", "차단한 내역을 찾을 수 없습니다."),
    BLOCK_PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "BLOCK4003", "차단 리스트를 확인할 수 없습니다. 유효하지 않은 페이지입니다."),

    // Chat Error
    CHATROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHATROOM4001", "채팅방을 찾을 수 없습니다."),
    CHATROOM_ACCESS_RESTRICTED(HttpStatus.FORBIDDEN, "CHATROOM4002", "채팅방을 만들 권한이 없습니다."),
    CHAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT4001", "채팅을 찾을 수 없습니다."),
    CHAT_ACCESS_RESTRICTED(HttpStatus.FORBIDDEN, "CHAT4002", "채팅을 보낼 권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}