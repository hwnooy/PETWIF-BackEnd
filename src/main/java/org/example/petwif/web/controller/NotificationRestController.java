package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.NotificationConverter;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.service.NotificationService.NotificationCommandService;
import org.example.petwif.service.NotificationService.NotificationQueryService;
import org.example.petwif.validation.annotation.ExistMember;
import org.example.petwif.web.dto.NotificationDTO.NotificationRequestDTO;
import org.example.petwif.web.dto.NotificationDTO.NotificationResponseDTO;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class NotificationRestController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @PostMapping("/{relatedMemberId}/send-notifications/{memberId}")
    @Operation(summary = "사용자의 알람 생성 API", description = "사용자가 어떠한 행위를 하여 다른 멤버에게 알람을 보내는 API입니다. 친구 관련 알람일 때는 RequestBody의 albumId을 입력하실 필요가 없지만, 앨범 관련 알람일 때는 RequestBody의 albumId를 필수로 입력하셔야 합니다.")
    @Parameters({
            @Parameter(name = "relatedMemberId", description = "알람을 보내는 사람(사용자), path variable 입니다!"),
            @Parameter(name = "memberId", description = "알람을 받는 사람, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationResultDTO> createdNotification(@RequestBody @Valid NotificationRequestDTO.NotificationDTO request,
                                                                                          @ExistMember @PathVariable(name = "relatedMemberId") Long relatedMemberId,
                                                                                          @ExistMember @PathVariable(name = "memberId") Long memberId) {
        Notification notification = notificationCommandService.createNotification(relatedMemberId, memberId, request);
        if (notification != null) return ApiResponse.onSuccess(NotificationConverter.toNotificationResultDTO(notification));
        else return ApiResponse.onSuccess(null);

    }

    @PutMapping("/{memberId}/notifications/setting/friend-requests")
    @Operation(summary = "사용자의 친구 요청 알람 활성화/비활성화 API", description = "사용자의 친구 요청 알람 활성화/비활성화하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationSettingDTO> notificationSettingFR(@ExistMember @PathVariable(name = "memberId") Long memberId) {
        Boolean setting = notificationCommandService.notificationSettingFR(memberId).getFriendRequestNoti();
        return ApiResponse.onSuccess(NotificationConverter.toNotificationSettingDTO(setting));
    }

    @PutMapping("/{memberId}/notifications/setting/friend-accepts")
    @Operation(summary = "사용자의 친구 요청 수락 알람 활성화/비활성화 API", description = "사용자의 친구 요청 수락 알람 활성화/비활성화하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationSettingDTO> notificationSettingFA(@ExistMember @PathVariable(name = "memberId") Long memberId) {
        Boolean setting = notificationCommandService.notificationSettingFA(memberId).getFriendAcceptNoti();
        return ApiResponse.onSuccess(NotificationConverter.toNotificationSettingDTO(setting));
    }

    @PutMapping("/{memberId}/notifications/setting/likes")
    @Operation(summary = "사용자의 좋아요 알람 활성화/비활성화 API", description = "사용자의 좋아요 알람 활성화/비활성화하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationSettingDTO> notificationSettingLK(@ExistMember @PathVariable(name = "memberId") Long memberId) {
        Boolean setting = notificationCommandService.notificationSettingLK(memberId).getLikeNoti();
        return ApiResponse.onSuccess(NotificationConverter.toNotificationSettingDTO(setting));
    }

    @PutMapping("/{memberId}/notifications/setting/bookmarks")
    @Operation(summary = "사용자의 북마크 알람 활성화/비활성화 API", description = "사용자의 북마크 알람 활성화/비활성화하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationSettingDTO> notificationSettingBM(@ExistMember @PathVariable(name = "memberId") Long memberId) {
        Boolean setting = notificationCommandService.notificationSettingBM(memberId).getBookmarkNoti();
        return ApiResponse.onSuccess(NotificationConverter.toNotificationSettingDTO(setting));
    }

    @PutMapping("/{memberId}/notifications/setting/comments")
    @Operation(summary = "사용자의 댓글 알람 활성화/비활성화 API", description = "사용자의 댓글 알람 활성화/비활성화하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationSettingDTO> notificationSettingCM(@ExistMember @PathVariable(name = "memberId") Long memberId) {
        Boolean setting = notificationCommandService.notificationSettingCM(memberId).getCommentNoti();
        return ApiResponse.onSuccess(NotificationConverter.toNotificationSettingDTO(setting));
    }

    @PutMapping("/{memberId}/notifications/{notificationId}")
    @Operation(summary = "사용자의 알람 확인 API", description = "사용자의 알람을 확인하는 API입니다. ResponseBody의 null이 아닌 Id를 이용해서 해당 페이지로 이동하시면 됩니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!"),
            @Parameter(name = "notificationId", description = "알람의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationOpenDTO> openNotification(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                                     @ExistMember @PathVariable(name = "notificationId") Long notificationId) {
        Notification notification = notificationCommandService.openNotification(memberId, notificationId);
        return ApiResponse.onSuccess(NotificationConverter.toNotificationOpenDTO(notification));
    }

    @GetMapping("/{memberId}/notifications")
    @Operation(summary = "사용자의 알람 목록 조회 API", description = "사용자의 알람 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationListDTO> getNotificationList(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                                        @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Slice<Notification> notificationList = notificationQueryService.getNotificationList(memberId, page);
        return ApiResponse.onSuccess(NotificationConverter.notificationListDTO(notificationList));
    }
}
