package org.example.petwif.repository;

import org.example.petwif.domain.entity.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImageRepository extends JpaRepository<CommentImage, Long> {
}