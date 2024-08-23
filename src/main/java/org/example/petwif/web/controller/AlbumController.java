package org.example.petwif.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.core.util.ApiResponsesDeserializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.petwif.albumConverter.AlbumConverter;
import org.example.petwif.apiPayload.ApiResponse;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.*;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.service.MemberService.MemberService;
import org.example.petwif.service.albumService.*;
import org.example.petwif.validation.annotation.ExistAlbum;
import org.example.petwif.web.dto.albumDto.AlbumRequestDto;
import org.example.petwif.web.dto.albumDto.AlbumResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class AlbumController {
    private final AlbumLikeService albumLikeService;
    private final AlbumService albumService;
    private final AlbumQueryService albumQueryService;
    private final AlbumBookmarkService albumBookmarkService;
    private final AlbumReportService albumReportService;
    private final MemberService memberService;

    //=======================================앨범 CRUD============================================//


    //==앨범 생성==//
    @PostMapping(value = "/albums", consumes = "multipart/form-data")
    @Operation(summary = "앨범 생성 API", description = "앨범을 생성하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<AlbumResponseDto.SaveResultDto> createAlbum(  @RequestPart(value = "requestDto") AlbumRequestDto.SaveRequestDto requestDto,
                                                                     @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                                     @RequestPart(value = "albumImages", required = false) MultipartFile[] albumImages,
                                                                     @RequestHeader("Authorization") String authorizationHeader ) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        Album album = albumService.saveAlbum(requestDto, member.getId(), coverImage, albumImages);
        AlbumResponseDto.SaveResultDto saveResultDto = AlbumConverter.toAlbumResultDto(album, member);
        return ApiResponse.onSuccess(saveResultDto);
    }


    //==앨범 수정==//
    @PatchMapping(value = "/albums/{albumId}")//, consumes = "multipart/form-data")
    @Operation(summary = "앨범 수정 API", description = "앨범을 생성 후 수정하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<AlbumResponseDto.UpdateResultDto> updateAlbum(@ExistAlbum @PathVariable("albumId") Long albumId,
                                                                     @RequestPart(value = "requestDto") AlbumRequestDto.UpdateRequestDto requestDto,
                                                                     //@RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
                                                                     //@RequestPart(value = "albumImages", required = false) MultipartFile[] albumImages,
                                                                     @RequestHeader("Authorization") String authorizationHeader){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Album updatedAlbum = albumService.updateAlbum(albumId, member.getId(), requestDto);//, coverImage, albumImages);
        return ApiResponse.onSuccess(AlbumConverter.UpdatedAlbumResultDto(updatedAlbum));
    }


    //==앨범 삭제==//
    @DeleteMapping("/albums/{albumId}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 삭제 API", description = "앨범을 삭제하는 API 입니다.")
    public ApiResponse<Void> deleteAlbum(@ExistAlbum @PathVariable("albumId") Long albumId, @RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getMemberByToken(authorizationHeader);

        albumService.deleteAlbum(albumId, member.getId());
        return ApiResponse.onSuccess(null);
    }


    //=======================================앨범 조회============================================//


    //== 1. 앨범 상세 페이지에서 앨범 조회 ==//
    @GetMapping("/albums/{albumId}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 상세 페이지 조회 API", description = "특정 앨범에 들어갔을 때 앨범 상세 페이지를 보는 API입니다.")
    public ApiResponse<AlbumResponseDto.DetailResultDto> getAlbumDetail(@ExistAlbum @PathVariable Long albumId, @RequestHeader("Authorization") String authorizationHeader){
        Member member = memberService.getMemberByToken(authorizationHeader);
        albumService.increaseView(albumId, member.getId());
        AlbumResponseDto.DetailResultDto albumDetail = albumQueryService.getAlbumDetails(albumId, member.getId());
        return ApiResponse.onSuccess(albumDetail);

    }


    //== 2-1. 메인 페이지에서 앨범 조회, 스토리형식 ==////slice완료

    @GetMapping("/stories")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "메인 페이지에서 스토리 형식으로 앨범 조회 API", description = "메인페이지에서 앨범 조회, 스토리 형식으로 조회하는 API입니다.")
    public ApiResponse<AlbumResponseDto.StoryAlbumListDto> getStoryAlbums(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name = "page", defaultValue = "0") Integer page){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<Album> albumSlice = albumQueryService.getStoryAlbum(member.getId(), page);
        return ApiResponse.onSuccess(AlbumConverter.convertToStoryAlbumResultListDto(albumSlice));
    }

    //== 2-2, 메인 페이지에서 앨범 조회, 게시글 형식 ==////slice완료
    @GetMapping("/posts")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "메인 페이지에서 게시글 형식으로 앨범 조회 API", description = "메인페이지에서 앨범 조회, 게시글 형식으로 조회하는 API입니다.")
    public ApiResponse<AlbumResponseDto.MainPageAlbumListDto> getMainpageAlbums(@RequestHeader("Authorization") String authorizationHeader,
                                                                                @RequestParam(name = "page", defaultValue = "0") Integer page){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<AlbumResponseDto.MainPageAlbumResultDto> albumSlice = albumQueryService.getMainpageAlbum(member.getId(), page);
        AlbumResponseDto.MainPageAlbumListDto albumListDto = AlbumConverter.convertToMainpageAlbumResultListDto(albumSlice);

        return ApiResponse.onSuccess(albumListDto);
    }

    //== 3. 탐색 페이지에서 앨범 조회 ==// //탐색 화면입니다.//slice완료
    @GetMapping("/albums/search")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "탐색 페이지에서 앨범 조회 API", description = "탐색 페이지에 들어갔을 때 앨범 리스트를 보는 API입니다.")
    public ApiResponse<AlbumResponseDto.SearchAlbumListDto> getSearchAlbums(@RequestHeader("Authorization") String authorizationHeader,
                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<Album> albumSlice = albumQueryService.getSearchableAlbums(member.getId(), page);
        return ApiResponse.onSuccess(AlbumConverter.convertToSearchAlbumListDto(albumSlice));

    }


    // 4. 사용자 페이지에서 앨범 조회 => 나, 다른사람 포함
    @GetMapping("/users/albums")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "사용자 앨범 조회 API", description = "사용자 페이지에서 앨범을 보는 API입니다. 정렬 방식은 최신(기본),좋아요,댓글,북마크 입니다.")
    public ApiResponse<AlbumResponseDto.UserAlbumViewListDto> getUserAlbums(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String nickname,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "sort_by", defaultValue = "LATEST") AlbumSortType sortType) {
        Member member = memberService.getMemberByToken(authorizationHeader);

        Member pageOwner = memberService.getMemberByNickname(nickname);
        Long pageOwnerId = pageOwner.getId();

        Slice<AlbumResponseDto.UserAlbumViewDto> albumSlice = albumQueryService.getMemberPageAlbums(pageOwnerId,member.getId(), page, sortType);
        AlbumResponseDto.UserAlbumViewListDto albumListDto = AlbumConverter.convertToUserAlbumViewListDto(albumSlice);

        return ApiResponse.onSuccess(albumListDto);
    }

    @GetMapping("/users/albums/searchTitle")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "사용자 앨범 조회에서 검색 한 화면 조회 API", description = "사용자 앨범 조회에서 검색을 들어가면 해당 앨범 제목이 있는 앨범들이 검색되고 검색된 앨범들이 조회되는 API 입니다. 검색 항목이 일치하는 앨범이 없을시 빈 리스트를 반환합니다")
    public ApiResponse<AlbumResponseDto.UserAlbumViewListDto> getSearchedUserAlbums(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String nickname,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "sort_by", defaultValue = "LATEST") AlbumSortType sortType,
            @RequestParam(name = "albumTitle") String albumTitle) {
        Member member = memberService.getMemberByToken(authorizationHeader);

        Member pageOwner = memberService.getMemberByNickname(nickname);
        Long pageOwnerId = pageOwner.getId();

        Slice<AlbumResponseDto.UserAlbumViewDto> albumSlice = albumQueryService.getSearchedMemberPageAlbums(pageOwnerId,member.getId(), page, sortType, albumTitle);
        AlbumResponseDto.UserAlbumViewListDto albumListDto = AlbumConverter.convertToUserAlbumViewListDto(albumSlice);

        return ApiResponse.onSuccess(albumListDto);
    }

    // 5. 북마크한 앨범 에서 앨범 조회 //slice완료
    @GetMapping("/albums/memberBookmark")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "북마크한 앨범 조회 API", description = "사용자가 북마크한 앨범 리스트를 보는 API입니다. 앨범 세부 페이지에서 북마크한 사람 보는것과 다릅니다.")
    public ApiResponse<AlbumResponseDto.MemberBookmarkAlbumListDto> getMemberBookmarkAlbums(@RequestHeader("Authorization") String authorizationHeader,
                                                                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                            @RequestParam(value = "sort_by", defaultValue = "LATEST") AlbumSortType sortType){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<AlbumResponseDto.MemberBookmarkAlbumDto> albumSlice = albumQueryService.getMemberBookmarkAlbums(member.getId(), page, sortType);
        AlbumResponseDto.MemberBookmarkAlbumListDto albumListDto = AlbumConverter.convertToMemberBookmarkAlbumListDto(albumSlice);
        return ApiResponse.onSuccess(albumListDto);
    }
    @GetMapping("/albums/memberBookmark/searchTitle")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "북마크한 앨범 조회에서 검색 한 화면 조회 API", description = "북마크한 앨범 조회에서 검색을 들어가면 해당 앨범 제목이 있는 앨범들이 검색되고 검색된 앨범들이 조회되는 API 입니다. 검색 항목이 일치하는 앨범이 없을시 빈 리스트를 반환합니다")
    public ApiResponse<AlbumResponseDto.MemberBookmarkAlbumListDto> getSearchedMemberBookmarkAlbums(@RequestHeader("Authorization") String authorizationHeader,
                                                                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                                    @RequestParam(value = "sort_by", defaultValue = "LATEST") AlbumSortType sortType,
                                                                                                @RequestParam(name = "albumTitle") String albumTitle){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<AlbumResponseDto.MemberBookmarkAlbumDto> albumSlice = albumQueryService.getSearchedMemberBookmarkAlbums(member.getId(), page, albumTitle, sortType);
        AlbumResponseDto.MemberBookmarkAlbumListDto albumListDto = AlbumConverter.convertToMemberBookmarkAlbumListDto(albumSlice);
        return ApiResponse.onSuccess(albumListDto);

    }



    //=======================================앨범 좋아요============================================//



    //==앨범 좋아요 생성==//
    @PostMapping("/albums/{albumId}/like")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 좋아요 API", description = "앨범 좋아요를 누르는 API 입니다. 앨범에서 좋아요 리스트를 누르면 볼수 있습니다.")
    public ApiResponse<Void> createAlbumLike(@ExistAlbum @PathVariable("albumId") Long albumId,
                                             @RequestHeader("Authorization") String authorizationHeader){
        Member member = memberService.getMemberByToken(authorizationHeader);
        albumLikeService.addAlbumLike(albumId, member.getId());
        return ApiResponse.onSuccess(null);
    }


    //==앨범 좋아요 취소==//
    @DeleteMapping("/albums/{albumId}/like")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 좋아요 취소 API", description = "앨범 좋아요를 취소하는 API 입니다. 좋아요가 없다면 에러")
    public ApiResponse<Void> deleteAlbumLike(@ExistAlbum @PathVariable Long albumId, @RequestHeader("Authorization") String authorizationHeader){
        Member member = memberService.getMemberByToken(authorizationHeader);
        albumLikeService.deleteAlbumLike(albumId, member.getId());
        return ApiResponse.onSuccess(null);
    }

    //==앨범 좋아요 리스트 조회==//
    @GetMapping("/albums/{albumId}/like")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 좋아요 리스트 조회 API", description = "특정 앨범에 대해 좋아요를 누른 사람의 목록이 나오는 API 입니다. 좋아요가 없다면 에러")
    public ApiResponse<AlbumResponseDto.LikeListDto> getLikeList(@ExistAlbum @PathVariable Long albumId,
                                                                  @RequestHeader("Authorization") String authorizationHeader,
                                                                  @RequestParam(name = "page", defaultValue = "0") Integer page){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<AlbumLike> albumLikeSlice = albumLikeService.getAlbumLikes(albumId, member.getId(), page);
        List<AlbumResponseDto.LikeResultDto> likes = albumLikeSlice.getContent().stream()
                .map(like -> new AlbumResponseDto.LikeResultDto(
                        like.getMember().getId(),
                        like.getMember().getNickname(),
                        like.getMember().getProfile_url()))
                .collect(Collectors.toList());

        AlbumResponseDto.LikeListDto likeListDto = AlbumResponseDto.LikeListDto.builder()
                .likes(likes)
                .listSize(likes.size())
                .isFirst(albumLikeSlice.isFirst())
                .isLast(albumLikeSlice.isLast())
                .hasNext(albumLikeSlice.hasNext())
                .build();
        return ApiResponse.onSuccess(likeListDto);
    }


    //=======================================앨범 북마크============================================//

    //==앨범 북마크 생성==//
    @PostMapping("/albums/{albumId}/bookmark")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 북마크 API", description = "앨범 북마크를 누르는 API입니다. 나의 북마크 에서 확인 가능")
    public ApiResponse<Void> creatAlbumBookmark(@ExistAlbum @PathVariable Long albumId, @RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getMemberByToken(authorizationHeader);
        albumBookmarkService.addBookmark(albumId, member.getId());
        return ApiResponse.onSuccess(null);
    }
    //==앨범 북마크 취소==//
    @DeleteMapping("/albums/{albumId}/bookmark")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 북마크 취소 API", description = "앨범 북마크를 취소하는 API입니다.")
    public ApiResponse<Void> deleteAlbumBookmark(@ExistAlbum @PathVariable Long albumId, @RequestHeader("Authorization") String authorizationHeader ){
        Member member = memberService.getMemberByToken(authorizationHeader);
            albumBookmarkService.deleteBookmark(albumId, member.getId());
            return ApiResponse.onSuccess(null);

    }

    //==앨범 북마크 조회==//
    @GetMapping("/albums/{albumId}/bookmark")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 북마크 리스트 조회 API", description = "앨범내애 북마크 버튼을 눌렀을 때 북마크 목록이 나오는 API 입니다")
    public ApiResponse<AlbumResponseDto.BookmarkListDto> getBookmarkList(@ExistAlbum @PathVariable Long albumId,
                                                                         @RequestHeader("Authorization") String authorizationHeader,
                                                                         @RequestParam(name = "page") Integer page){
        Member member = memberService.getMemberByToken(authorizationHeader);
        Slice<AlbumBookmark> memberBookmarkSlice = albumBookmarkService.getAlbumBookmarks(albumId, member.getId(), page);
        List<AlbumResponseDto.BookmarkResultDto> bookmarks = memberBookmarkSlice.getContent().stream()
                .map(bookmark -> new AlbumResponseDto.BookmarkResultDto(
                        bookmark.getMember().getId(),
                        bookmark.getMember().getNickname(),
                        bookmark.getMember().getProfile_url()))
                .collect(Collectors.toList());

        AlbumResponseDto.BookmarkListDto bookmarkListDto = AlbumResponseDto.BookmarkListDto.builder()
                .bookmarks(bookmarks)
                .listSize(bookmarks.size())
                .isFirst(memberBookmarkSlice.isFirst())
                .isLast(memberBookmarkSlice.isLast())
                .hasNext(memberBookmarkSlice.hasNext())
                .build();
        return ApiResponse.onSuccess(bookmarkListDto);
    }

    //=======================================앨범 신고============================================//
    //== 앨범 신고==//
    @PostMapping("/albums/{albumId}/report")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Operation(summary = "앨범 신고 API", description = "특정 앨범을 신고하는 API입니다.")
    public ApiResponse<Void> creatAlbumReport(@ExistAlbum @PathVariable Long albumId, @RequestHeader("Authorization") String authorizationHeader, @RequestBody AlbumRequestDto.ReportRequestDto requestDto){
        Member member = memberService.getMemberByToken(authorizationHeader);
        albumReportService.doReport(albumId, member.getId(), requestDto.getReportReason());
        return ApiResponse.onSuccess(null);
    }



}
