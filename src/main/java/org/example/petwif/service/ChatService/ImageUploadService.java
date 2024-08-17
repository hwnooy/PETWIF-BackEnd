package org.example.petwif.service.ChatService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.S3.AmazonS3Manager;
import org.example.petwif.S3.Uuid;
import org.example.petwif.converter.ChatConverter;
import org.example.petwif.domain.entity.Chat;
import org.example.petwif.domain.entity.ChatImage;
import org.example.petwif.repository.ChatImageRepository;
import org.example.petwif.repository.ChatRepository;
import org.example.petwif.repository.UuidRepository;
import org.example.petwif.web.dto.ChatDTO.ChatRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class ImageUploadService {

    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final ChatImageRepository chatImageRepository;

    public String uploadImage(MultipartFile file) {
        ChatRequestDTO.imageUploadDTO requestImage = new ChatRequestDTO.imageUploadDTO();
        ChatRequestDTO.SendChatDTO requestChat = new ChatRequestDTO.SendChatDTO();
        Chat chat = ChatConverter.toChat(requestChat);

        List<ChatImage> chatImages = Optional.ofNullable(requestImage.getChatImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(multipartFile -> {
                    try {
                        String uuid = UUID.randomUUID().toString() + ".jpg";
                        System.out.println("Generated UUID: " + uuid);
                        Uuid savedUuid = uuidRepository.save(
                                Uuid.builder()
                                        .uuid(uuid)
                                        .build()
                        );
                        String pictureUrl = s3Manager.uploadFile(s3Manager.generateChatKeyName(savedUuid), multipartFile);
                        return ChatConverter.toChatImage(pictureUrl, chat);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!chatImages.isEmpty()) {
            chatImageRepository.saveAll(chatImages);
        }
        return "The image has been uploaded";
    }
}
