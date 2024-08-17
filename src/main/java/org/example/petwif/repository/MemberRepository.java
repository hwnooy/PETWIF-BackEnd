package org.example.petwif.repository;

import org.example.petwif.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    //Member findByEmail(String email);
    Member findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    @Query("select m from Member m where m.id = :mId")
    Member findByMemberId(@Param("mId")Long id);

    @Query("select m from Member m where m.email = :mail")
    Optional<Member> findMemberByEmail(@Param("mail") String mail);
    @Query("select m from Member m where m.email = :mail and m.oauthProvider = :oauth")
    Optional<Member> checkEmail(@Param("mail") String mail, @Param("oauth") String oauth);


    @Query("select m from Member m where m.nickname = :nickname")
    Optional<Member> checkNickname(@Param("nickname") String name);

//    @Query("select m from Member m where m.oauthProvider = :oauth and m.email=:email")
//    Optional<Member> checkGoogleEmail(@Param("oauth") String oauth, @Param("email") String email);

    //@Query("delete from Member m where m.id=:id")

    @Override
    void deleteById(Long id);

    // 메인페이지에서 친구의 앨범을 스토리 형태로 조회하기 위해 추가한 메서드 입니다. - 현일
    @Query("SELECT f.friend FROM Friend f WHERE f.member.id = :memberId AND f.status = 'ACCEPTED'")
    List<Member> findFriendsByMemberId(@Param("memberId") Long memberId);
}
