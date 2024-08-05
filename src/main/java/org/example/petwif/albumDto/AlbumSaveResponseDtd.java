package org.example.petwif.albumDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AlbumSaveResponseDtd {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveResultDtd{
        Long memberId;
        LocalDateTime createdAt;
    }
}
