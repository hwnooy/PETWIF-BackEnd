package org.example.petwif.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.converter.ChatConverter;
import org.example.petwif.domain.entity.Chat;
import org.example.petwif.domain.entity.ChatReport;
import org.example.petwif.domain.entity.ChatRoom;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.service.ChatService.ChatCommandService;
import org.example.petwif.service.ChatService.ChatQueryService;

import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.validation.annotation.ExistChatRoom;
import org.example.petwif.web.dto.ChatDTO.ChatRequestDTO;
import org.example.petwif.web.dto.ChatDTO.ChatResponseDTO;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Optional;

@RestController
@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/chats")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;
    private final MemberService memberService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor("",true));
    }

    //채팅 생성 - 완
    @PostMapping("/chatRoom")
    @Operation(summary = "채팅방 생성 API")
    public ApiResponse<ChatResponseDTO.CreateChatRoomResultDTO> createChatRoom(@RequestHeader("Authorization") String authorizationHeader,
                                                                               @RequestBody ChatRequestDTO.CreateChatRoomDTO request) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        Member other = memberService.getMemberByNickname(request.getNickname());
        Long otherId = other.getId();

        ChatRoom chatRoom = chatCommandService.createChatRoom(memberId, otherId, request);
        return ApiResponse.onSuccess(ChatConverter.createChatRoomResultDTO(chatRoom));
    }

    //채팅 보내기 - 완 (Rest API)
    @PostMapping(value = "/chatRoom/{chatRoomId}/send", consumes = "multipart/form-data")
    @Operation(summary = "채팅 메시지 전송 API - Rest API")
    public ApiResponse<ChatResponseDTO.SendChatResultDTO> sendChat(//@ModelAttribute ChatRequestDTO.SendChatDTO request,
                                                                   @RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestParam String content,
                                                                   @RequestPart(value = "chatImage", required = false) MultipartFile chatImage,
                                                                   @ExistChatRoom @PathVariable(name = "chatRoomId") Long chatRoomId) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        ChatRequestDTO.SendChatDTO request = new ChatRequestDTO.SendChatDTO();
        request.setContent(content);

        Chat chat = chatCommandService.sendChat(memberId, chatRoomId, request, chatImage);
        return ApiResponse.onSuccess(ChatConverter.sendChatResultDTO(chat));
    }

    //채팅 신고
    @PostMapping("/chatRoom/{chatRoomId}/report")
    @Operation(summary = "채팅 신고 API")
    public ApiResponse<ChatResponseDTO.ReportChatResultDTO> reportChat(@RequestHeader ("Authorization") String authorizationHeader,
                                                                       @RequestBody ChatRequestDTO.ReportChatDTO request,
                                                                       @PathVariable(name = "chatRoomId") Long chatRoomId,
                                                                       @RequestParam(name = "chatId") Long chatId) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        ChatReport chatReport = chatCommandService.reportChat(memberId, chatRoomId, chatId, request);
        return ApiResponse.onSuccess(ChatConverter.reportChatResultDTO(chatReport));
    }

    //채팅창 화면 조회 - 완
    @GetMapping("/chatRoom/{chatRoomId}")
    @Operation(summary = "채팅창 화면 조회 API", description = "채팅창 화면 (채팅방의 대화 내용) 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "chatRoomId", description = "채팅방 아이디, path variable 입니다!")
    })
    public ApiResponse<ChatResponseDTO.ChatPreviewListDTO> getChatList(@ExistChatRoom @PathVariable(name = "chatRoomId") Long chatRoomId,
                                                                       @RequestParam(name = "page") Integer page) {
        Slice<Chat> chatList = chatQueryService.getChatList(chatRoomId, page);
        return ApiResponse.onSuccess(ChatConverter.toChatPreviewListDTO(chatList));
    }

    //채팅방 목록 조회 - 완
    @GetMapping("/")
    @Operation(summary = "채팅방 목록 조회 API", description = "전체 채팅방 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })

    public ApiResponse<ChatResponseDTO.ChatRoomPreviewListDTO> getChatRoomList(@RequestHeader("Authorization") String authorizationHeader,
                                                                               @RequestParam(name = "page") Integer page) {
        Member member = memberService.getMemberByToken(authorizationHeader);

        Long memberId = member.getId();

        Slice<ChatRoom> chatRoomList = chatQueryService.getChatRoomList(memberId, memberId, page);
        return ApiResponse.onSuccess(ChatConverter.toChatRoomPreviewListDTO(chatRoomList));
    }

    //가장 최근 채팅 조회 - 완
    @GetMapping("/chatRoom/{chatRoomId}/last-chat")
    @Operation(summary = "가장 최근 채팅 조회 API", description = "가장 최근 전송된 채팅을 조회하는 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "chatRoomId")
    })
    public ApiResponse<ChatResponseDTO.LastChatPreviewDTO> getLastChat(@ExistChatRoom @PathVariable(name = "chatRoomId") Long chatRoomId) {
        Optional<Chat> lastChat = chatQueryService.getLastChat(chatRoomId);
        return ApiResponse.onSuccess(ChatConverter.toLastChatPreviewDTO(lastChat.get()));
    }

    //채팅방 나가기 - 완
    @DeleteMapping("/chatRoom/{chatRoomId}")
    @Operation(summary = "채팅방 나가기 API")
    public ApiResponse<Void> deleteChatRoom(@RequestHeader("Authorization") String authorizationHeader, @PathVariable(name = "chatRoomId") Long chatRoomId) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Long memberId = member.getId();

        chatCommandService.deleteChatRoom(memberId, chatRoomId);
        return ApiResponse.onSuccess(null);
    }
}
