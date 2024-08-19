package org.example.petwif.repository;


import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.domain.enums.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    //List<Album> findAlbumsByMember(List<Member> friends, Scope FRIEND);

    //3. 탐색 페이지에서 앨범 조회 서비스에 필요한 메서드
    List<Album> findAllByOrderByUpdatedAtDesc();

    List<Album> findAlbumsByMemberId(Long pageOwnerId);

    List<Album> findByMemberIdOrderByUpdatedAtDesc(Long memberId);

   /* Slice<Album> findStoryAlbumsByMemberId(Long memberId, PageRequest pageRequest);

    @Query("SELECT a FROM Album a WHERE a.member.id IN :friendIds ORDER BY a.createdAt DESC")
    Slice<Album> findStoryAlbumsByMemberIds(List<Long> friendIds, Pageable pageable);*/

}
