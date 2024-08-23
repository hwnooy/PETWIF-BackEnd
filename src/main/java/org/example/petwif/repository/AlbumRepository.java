package org.example.petwif.repository;


import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.AlbumSortType;
import org.example.petwif.domain.enums.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT a FROM Album a WHERE a.member.id IN :friendIds ORDER BY a.updatedAt DESC")
    Slice<Album> findByMemberIdInOrderByUpdatedAtDesc(List<Long> friendIds, PageRequest pageRequest);

    @Query("SELECT a FROM Album a WHERE a.member.id NOT IN :friendIds AND a.scope = :scope ORDER BY a.updatedAt DESC ")
    Slice<Album> findPublicAlbumsByNonFriends(List<Long> friendIds, Scope scope, PageRequest pageRequest);


    @Query("SELECT a FROM Album a WHERE a.member.id IN :allMemberIds ORDER BY a.updatedAt DESC")
    Slice<Album> findAllByMemberIds(@Param("allMemberIds") List<Long> memberIds, PageRequest pageRequest);

    @Query("SELECT a FROM Album a WHERE a.member.id = :currentUserId ORDER BY a.updatedAt DESC ")
    Slice<Album> findAlbumByMemberId(@Param("currentUserId") Long currentUserId);


    @Query("SELECT a FROM Album a WHERE a.id IN :ids order by a.updatedAt DESC ")
    Slice<Album> findAllByIds(@Param("ids") List<Long> ids, PageRequest pageRequest);

    @Query("SELECT a FROM Album a WHERE a.id IN :ids AND a.title LIKE %:title%")
    Slice<Album> findAllByIdsAndTitleContaining(@Param("ids") List<Long> ids, @Param("title") String title, PageRequest pageRequest);

}
