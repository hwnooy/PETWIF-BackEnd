package org.example.petwif.repository;

import org.example.petwif.domain.entity.ChatImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatImageRepository extends JpaRepository<ChatImage, Long> {
}
