package org.example.petwif.web.dto.albumDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.entity.AlbumImage;
import org.example.petwif.domain.enums.Scope;
import org.example.petwif.domain.enums.StickerType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public class AlbumRequestDto {
    //==앨범 CRUD==//
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequestDto{
        private String title;
        private String content;
        private Scope scope;
   /*     private MultipartFile coverImage;  // 변경: String -> MultipartFile
        private List<MultipartFile> albumImages;  // 변경: String -> MultipartFile*/

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequestDto {
        private String title;
        private String content;
        private Scope scope;
    }


    //==앨범 좋아요==//
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeRequestDto{
        private Long memberId;
    }


    //==앨범 북마크==//
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkRequestDto{
        private Long memberId;
    }

    //==앨범 신고 사유 ==//

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportRequestDto{
        private String reportReason;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StickerRequestDto{
        private String stickerName;
        StickerType stickerType;
    }

}
