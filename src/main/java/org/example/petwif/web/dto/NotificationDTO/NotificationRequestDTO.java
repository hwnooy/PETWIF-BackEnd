package org.example.petwif.web.dto.NotificationDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

public class NotificationRequestDTO {

    @Getter
    public static class NotificationDTO {
        Long albumId;
        @Enumerated(EnumType.STRING)
        String dtype;
    }
}
