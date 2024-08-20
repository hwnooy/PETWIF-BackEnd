package org.example.petwif.repository;

import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.FriendStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByMember_IdAndFriend_Id(Long memberId, Long friendId);

    Slice<Friend> findByMember_IdAndStatusOrderByCreatedAt(Long memberId, FriendStatus status, PageRequest pageRequest);

    @Query("SELECT DISTINCT f2 FROM Friend f1 " +
            "JOIN Friend f2 ON f2.member.id = f1.friend.id " +
            "WHERE f1.member.id = :memberId " +
            "AND f1.status = :status " +
            "AND f2.status = :status " +
            "AND f2.friend.id NOT IN :blockedMemberIds " +
            "AND f2.friend.id != :memberId " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM Friend f3 " +
            "   WHERE f3.member.id = :memberId " +
            "   AND f3.friend.id = f2.friend.id " +
            "   AND f3.status = :status" +
            ") " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM Friend fr1 " +
            "   WHERE fr1.member.id = :memberId " +
            "   AND fr1.friend.id = f2.friend.id " +
            "   AND fr1.status = 'PENDING'" +
            ") " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM Friend fr2 " +
            "   WHERE fr2.member.id = f2.friend.id " +
            "   AND fr2.friend.id = :memberId " +
            "   AND fr2.status = 'PENDING'" +
            ") " +
            "GROUP BY f2.friend.id")
    Slice<Friend> findFriendsOfFriends(@Param("memberId") Long memberId,
                                       @Param("status") FriendStatus status,
                                       @Param("blockedMemberIds") List<Long> blockedMemberIds,
                                       PageRequest pageRequest);





    // memberId와 friendId를 사용해서 친구 관계가 있는지 확인하는 쿼리 입니다. 앨범 공개 범위 로직을 위해 추가했습니다! - 현일
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE " +
            "((f.member.id = :memberId AND f.friend.id = :friendId AND f.status = :status) OR " +
            "(f.member.id = :friendId AND f.friend.id = :memberId AND f.status = :status))")
    boolean isFriend(@Param("memberId") Long memberId, @Param("friendId") Long friendId, @Param("status") FriendStatus status);



    //친구 찾기리스트
    List<Friend> findByMember_IdAndStatus(Long memberId, FriendStatus friendStatus);
}
