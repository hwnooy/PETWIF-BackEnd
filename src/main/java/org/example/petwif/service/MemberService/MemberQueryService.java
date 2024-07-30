package org.example.petwif.service.MemberService;

import org.example.petwif.domain.entity.Member;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);
}
