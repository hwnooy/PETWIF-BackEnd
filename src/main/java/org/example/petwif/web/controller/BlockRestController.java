package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.BlockConverter;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.service.BlockService.BlockCommandService;
import org.example.petwif.service.BlockService.BlockQueryService;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.web.dto.BlockDTO.BlockRequestDTO;
import org.example.petwif.web.dto.BlockDTO.BlockResponseDTO;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/blocks")
public class BlockRestController {

    private final BlockCommandService blockCommandService;;
    private final BlockQueryService blockQueryService;
    private final MemberService memberService;

    @PostMapping("")
    @Operation(summary = "멤버 차단 API", description = "사용자가 다른 멤버를 차단하는 API입니다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT 토큰으로, 사용자의 아이디, request header 입니다!")
    })
    public ApiResponse<BlockResponseDTO.BlockResultDTO> blockMember(@RequestHeader("Authorization") String authorizationHeader,
                                                                    @RequestBody @Valid BlockRequestDTO.BlockDTO request) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        Member target = memberService.getMemberByNickname(request.getNickname());
        Long targetId = target.getId();

        Block block = blockCommandService.blockMember(memberId, targetId);
        return ApiResponse.onSuccess(BlockConverter.toBlockResultDTO(block));
    }

    @GetMapping("/status")
    @Operation(summary = "다른 멤버와의 차단 상태 조회 API", description = "사용자가 다른 멤버와의 차단 상태를 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT 토큰으로, 사용자의 아이디, request header 입니다!")
    })
    public ApiResponse<BlockResponseDTO.BlockStatusDTO> getBlockStatus(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @RequestBody @Valid BlockRequestDTO.BlockDTO request) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        Member target = memberService.getMemberByNickname(request.getNickname());
        Long targetId = target.getId();

        boolean blockStatus = blockQueryService.getBlockStatus(memberId, targetId);
        return ApiResponse.onSuccess(BlockConverter.toBlockStatusDTO(blockStatus));
    }

    @GetMapping("")
    @Operation(summary = "사용자의 차단 목록 조회 API", description = "사용자의 차단 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT 토큰으로, 사용자의 아이디, request header 입니다!"),
            @Parameter(name = "page", description = "현재 페이지, query parameter 입니다!")
    })
    public ApiResponse<BlockResponseDTO.BlockListDTO> getBlockList(@RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestParam(name = "page" ,defaultValue = "0") Integer page) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        Slice<Block> blockList = blockQueryService.getBlockList(memberId, page);
        return ApiResponse.onSuccess(BlockConverter.blockListDTO(blockList));
    }

    @DeleteMapping("")
    @Operation(summary = "멤버 차단 해제 API", description = "사용자가 다른 멤버에 대한 차단을 해제하는 API입니다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT 토큰으로, 차단 해제를 하는 멤버의 아이디(사용자), request header 입니다!")
    })
    public ApiResponse<Void> unblockMember(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody @Valid BlockRequestDTO.BlockDTO request) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        Member target = memberService.getMemberByNickname(request.getNickname());
        Long targetId = target.getId();

        blockCommandService.unblockMember(memberId, targetId);
        return ApiResponse.onSuccess(null);
    }
}
