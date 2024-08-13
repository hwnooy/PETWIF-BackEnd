package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.NotificationConverter;
import org.example.petwif.domain.entity.Notification;
import org.example.petwif.service.NotificationService.NotificationCommandService;
import org.example.petwif.service.NotificationService.NotificationQueryService;
import org.example.petwif.validation.annotation.ExistMember;
import org.example.petwif.web.dto.NotificationDTO.NotificationResponseDTO;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @GetMapping("/{memberId}/notifications")
    @Operation(summary = "특정 멤버의 알람 목록 조회 API", description = "특정 멤버의 알람 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "memberId", description = "멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<NotificationResponseDTO.NotificationListDTO> getNotificationList(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                                        @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Slice<Notification> notificationList = notificationQueryService.getNotificationList(memberId, page);
        return ApiResponse.onSuccess(NotificationConverter.notificationListDTO(notificationList));
    }
}
