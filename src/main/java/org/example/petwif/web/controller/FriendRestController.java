package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.FriendConverter;
import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.service.FriendService.FriendCommandService;
import org.example.petwif.service.FriendService.FriendQueryService;
import org.example.petwif.validation.annotation.ExistMember;
import org.example.petwif.web.dto.FriendDTO.FriendResponseDTO;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class FriendRestController {

    private final FriendCommandService friendCommandService;
    private final FriendQueryService friendQueryService;

    @PostMapping("/{memberId}/friends/{friendId}/requests")
    @Operation(summary = "친구 요청 API", description = "사용자가 다른 멤버에게 친구 요청하는 API입니다. 'PENDING'은 친구 요청을 기다리는 상태입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "친구 요청을 하는 멤버의 아이디(사용자), path variable 입니다!"),
            @Parameter(name = "friendId", description = "친구 요청 당하는 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendResultDTO> friendRequest(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                        @ExistMember @PathVariable(name = "friendId") Long friendId) {
        Friend friend = friendCommandService.friendRequest(memberId, friendId);
        return ApiResponse.onSuccess(FriendConverter.toFriendResultDTO(friend));
    }

    @PutMapping("/{memberId}/friend/{friendId}/cancellations")
    @Operation(summary = "친구 요청 취소 API", description = "사용자가 다른 멤버에게 한 친구 요청을 취소하는 API입니다. 'CANCEL'은 친구 요청을 취소한 상태입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "친구 요청을 취소하는 멤버의 아이디(사용자), path variable 입니다!"),
            @Parameter(name = "friendId", description = "친구 요청 취소 당하는 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendResultDTO> friendCancel(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                       @ExistMember @PathVariable(name = "friendId") Long friendId) {
        Friend friend = friendCommandService.friendCancel(memberId, friendId);
        return ApiResponse.onSuccess(FriendConverter.toFriendResultDTO(friend));
    }

    @PutMapping("/{memberId}/friend/{friendId}/acceptances")
    @Operation(summary = "친구 요청 수락 API", description = "사용자에게 온 친구 요청에 대해 수락하는 API입니다. 'ACCEPT'는 친구 요청을 수락한 상태로 친구 상태를 의미하며, 두 유저 모두에게 적용됩니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "친구 요청을 수락하는 멤버의 아이디(사용자), path variable 입니다!"),
            @Parameter(name = "friendId", description = "친구 요청을 했던 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendResultDTO> friendAccept(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                       @ExistMember @PathVariable(name = "friendId") Long friendId) {
        Friend friend = friendCommandService.friendAccept(memberId, friendId);
        return ApiResponse.onSuccess(FriendConverter.toFriendResultDTO(friend));
    }

    @PutMapping("/{memberId}/friend/{friendId}/rejections")
    @Operation(summary = "친구 요청 거절 API", description = "사용자에게 온 친구 요청에 대해 거절하는 API입니다. 'REJECT'는 친구 요청을 거절한 상태로 친구 요청을 걸기 전과 같은 아무것도 아닌 상태가 되었음을 의미하며, 두 유저 모두에게 적용됩니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "친구 요청을 거절하는 멤버의 아이디(사용자), path variable 입니다!"),
            @Parameter(name = "friendId", description = "친구 요청을 했던 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendResultDTO> friendReject(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                       @ExistMember @PathVariable(name = "friendId") Long friendId) {
        Friend friend = friendCommandService.friendReject(memberId, friendId);
        return ApiResponse.onSuccess(FriendConverter.toFriendResultDTO(friend));
    }

    @GetMapping("/{memberId}/friends/{friendId}")
    @Operation(summary = "다른 멤버와의 친구 상태 조회 API", description = "사용자와 다른 멤버와의 친구 상태를 조회하는 API입니다. 'ACCEPT'는 친구 상태, 'PENDING'은 요청 중인 상태, 'null', 'CANCEL', 'REJECT'는 친구가 아닌 상태입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!"),
            @Parameter(name = "friendId", description = "다른 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendStatusDTO> getFriendStatus(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                          @ExistMember @PathVariable(name = "friendId") Long friendId) {
        FriendStatus friendStatus = friendQueryService.getFriendStatus(memberId, friendId);
        return ApiResponse.onSuccess(FriendConverter.toFriendStatusDTO(friendStatus));
    }

    @GetMapping("/{memberId}/friends")
    @Operation(summary = "사용자의 친구 목록 조회 API", description = "사용자의 친구 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendListDTO> getFriendList(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                      @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Slice<Friend> friendList = friendQueryService.getFriendList(memberId, page);
        return ApiResponse.onSuccess(FriendConverter.friendListDTO(friendList));
    }

    @GetMapping("/{memberId}/recfriends")
    @Operation(summary = "사용자의 추천 친구 목록 조회 API", description = "사용자의 추천 친구 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "memberId", description = "사용자의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.RecFriendListDTO> getRecFriendList(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Slice<Friend> recFriendList = friendQueryService.getRecFriendList(memberId, page);
        return ApiResponse.onSuccess(FriendConverter.recFriendListDTO(recFriendList, memberId));
    }

    @DeleteMapping("/{memberId}/friends/{friendId}")
    @Operation(summary = "친구 삭제 API", description = "사용자가 다른 멤버와의 친구 관계를 삭제하는 API입니다.")
    @Parameters({
            @Parameter(name = "memberId", description = "친구를 삭제하는 멤버의 아이디(사용자), path variable 입니다!"),
            @Parameter(name = "friendId", description = "삭제당한 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<FriendResponseDTO.FriendResultDTO> deleteFriend(@ExistMember @PathVariable(name = "memberId") Long memberId,
                                                                       @ExistMember @PathVariable(name = "friendId") Long friendId) {
        friendCommandService.deleteFriend(memberId, friendId);
        return ApiResponse.onSuccess(null);
    }
}
