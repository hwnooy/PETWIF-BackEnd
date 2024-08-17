package org.example.petwif.web.dto.ChatDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ChatRequestDTO {

    @Getter
    public static class CreateChatRoomDTO { //채팅 생성

        @NotNull
        private Long otherId;
    }

    @Getter
    @Setter
    public static class SendChatDTO { //채팅 보내기
        @NotBlank
        private String content;
        @Nullable
        private List<MultipartFile> chatImages = new ArrayList<>();
    }
}