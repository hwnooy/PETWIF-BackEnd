package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.BlockConverter;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.service.BlockService.BlockCommandService;
import org.example.petwif.service.BlockService.BlockQueryService;
import org.example.petwif.validation.annotation.ExistMember;
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

    @PostMapping("/")
    @Operation(summary = "멤버 차단 API", description = "특정 멤버가 다른 멤버를 차단하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 형식이 잘못됨", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "차단을 하는 멤버의 아이디, query parameter 입니다!"),
            @Parameter(name = "targetId", description = "차단을 당하는 멤버의 아이디, query parameter 입니다!")
    })
    public ApiResponse<BlockResponseDTO.BlockResultDTO> doBlock(@ExistMember @RequestParam(name = "memberId") Long memberId,
                                                                @ExistMember @RequestParam(name = "targetId") Long targetId) {
        Block block = blockCommandService.doBlock(memberId, targetId);
        return ApiResponse.onSuccess(BlockConverter.toBlockResultDTO(block));
    }

    @GetMapping("/{memberId}/blocks")
    @Operation(summary = "특정 멤버의 차단 목록 조회 API", description = "특정 멤버의 차단 목록을 조회하는 API이며, 페이징을 포함합니다. query String으로 page 번호를 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<BlockResponseDTO.BlockListDTO> getBlockList(@ExistMember @PathVariable(name = "memberId") Long memberId, @RequestParam(name = "page") Integer page) {
        Slice<Block> blockList = blockQueryService.getBlockList(memberId, page);
        return ApiResponse.onSuccess(BlockConverter.blockListDTO(blockList));
    }

    @DeleteMapping("/{targetId}")
    @Operation(summary = "멤버 차단 해제 API", description = "특정 멤버가 다른 멤버에 대한 차단을 해제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "memberId", description = "차단 해제를 하는 멤버의 아이디, query parameter 입니다!"),
            @Parameter(name = "targetId", description = "차단 해제를 당하는 멤버의 아이디, path variable 입니다!")
    })
    public ApiResponse<Void> deleteBlock(@ExistMember @RequestParam(name = "memberId") Long memberId,
                                         @ExistMember @PathVariable(name = "targetId") Long targetId) {
        blockCommandService.deleteBlock(memberId, targetId);
        return ApiResponse.onSuccess(null);
    }
}
