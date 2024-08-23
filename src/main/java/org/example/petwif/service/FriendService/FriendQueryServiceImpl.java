package org.example.petwif.service.FriendService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.handler.FriendHandler;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.repository.BlockRepository;
import org.example.petwif.repository.FriendRepository;
import org.example.petwif.repository.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendQueryServiceImpl implements FriendQueryService {

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    private final BlockRepository blockRepository;

    @Override
    public FriendStatus getFriendStatus(Long memberId, Long friendId) {

        Friend friend = friendRepository.findByMember_IdAndFriend_Id(memberId, friendId).orElse(new Friend());

        FriendStatus friendStatus = friend.getStatus();

        return friendStatus;
    }

    @Override
    public Slice<Friend> getFriendList(Long memberId, Integer page, FriendStatus friendStatus) {

        Slice<Friend> friendList = friendRepository.findByMember_IdAndStatusOrderByCreatedAt(memberId, friendStatus, PageRequest.of(page, 6));

        if (friendList.isEmpty() && page != 0) {
            throw new FriendHandler(ErrorStatus.FRIEND_PAGE_NOT_FOUND);
        }

        return friendList;
    }

    @Override
    public Slice<Friend> getRecFriendList(Long memberId, Integer page) {

        List<Block> blockList = blockRepository.findByMember_Id(memberId);
        List<Long> blockedMembers = blockList.stream()
                .map(block -> block.getTarget().getId())
                .collect(Collectors.toList());


        Slice<Friend> recFriendList = friendRepository.findFriendsOfFriends(memberId, FriendStatus.ACCEPTED, blockedMembers, PageRequest.of(page, 5));

        if (recFriendList.isEmpty() && page != 0) {
            throw new FriendHandler(ErrorStatus.FRIEND_PAGE_NOT_FOUND);
        }

        return recFriendList;
    }

    @Override
    public Optional<Friend> findFriend(Long Id) {
        return friendRepository.findById(Id);
    }
}
