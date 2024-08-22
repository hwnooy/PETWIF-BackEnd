package org.example.petwif.service.BlockService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.handler.BlockHandler;
import org.example.petwif.apiPayload.exception.handler.FriendHandler;
import org.example.petwif.domain.entity.Block;
import org.example.petwif.domain.entity.Friend;
import org.example.petwif.domain.enums.FriendStatus;
import org.example.petwif.repository.BlockRepository;
import org.example.petwif.repository.FriendRepository;
import org.example.petwif.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockCommandServiceImpl implements BlockCommandService {

    private final MemberRepository memberRepository;

    private final BlockRepository blockRepository;

    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public Block blockMember(Long memberId, Long targetId) {

        Block block = new Block();

        if (blockRepository.existsByMember_IdAndTarget_Id(memberId, targetId)) {
            throw new BlockHandler(ErrorStatus.BLOCK_ALREADY_EXIST);
        }

        block.setMember(memberRepository.findById(memberId).get());
        block.setTarget(memberRepository.findById(targetId).get());

        if (friendRepository.existsByMember_IdAndFriend_Id(memberId, targetId) && friendRepository.existsByMember_IdAndFriend_Id(targetId, memberId)) {
            Friend friend = friendRepository.findByMember_IdAndFriend_Id(targetId, memberId).orElseThrow(() -> new FriendHandler(ErrorStatus.FRIEND_NOT_FOUND));
            Friend me = friendRepository.findByMember_IdAndFriend_Id(memberId, targetId).orElseThrow(() -> new FriendHandler(ErrorStatus.FRIEND_NOT_FOUND));

            if (friend.getStatus() == FriendStatus.ACCEPTED && me.getStatus() == FriendStatus.ACCEPTED) {
                friendRepository.delete(friend);
                friendRepository.delete(me);
            }
        }

        return blockRepository.save(block);
    }

    @Override
    @Transactional
    public void unblockMember(Long memberId, Long targetId) {
        Block block = blockRepository.findByMember_IdAndTarget_Id(memberId, targetId).orElseThrow(() -> new BlockHandler(ErrorStatus.BLOCK_NOT_FOUND));

        blockRepository.delete(block);
    }
}
