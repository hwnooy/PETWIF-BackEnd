package org.example.petwif.service.FriendService;

import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.enums.FriendStatus;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface FriendQueryService {

    FriendStatus getFriendStatus(Long memberId, Long friendId);
    Slice<Friend> getFriendList(Long memberId, Integer page);
    Slice<Friend> getRecFriendList(Long memberId, Integer page);
    Optional<Friend> findFriend(Long Id);
}
