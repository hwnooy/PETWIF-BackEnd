package org.example.petwif.repository;

import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Comment;
import org.example.petwif.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
