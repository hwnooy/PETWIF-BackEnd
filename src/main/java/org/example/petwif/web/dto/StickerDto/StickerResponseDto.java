package org.example.petwif.web.dto.StickerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petwif.domain.enums.StickerType;

import java.util.List;


public class StickerResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StickerResultDto{
        private Long id;
        private String stickerName;
        private String stickerImageUrl;
        private StickerType stickerType;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StickerResultListDto{
        List<StickerResultDto> stickerResultDtoList;
    }

}

