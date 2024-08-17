package org.example.petwif.service.albumService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.apiPayload.code.status.ErrorStatus;
import org.example.petwif.apiPayload.exception.GeneralException;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.repository.BlockRepository;
import org.example.petwif.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*앨범 차단과 범위 확인 메서드 입니다!!
* Service가 Service를 참조해서 놀라셨겠지만 급하다 보니 이렇게 했습니다... 혹시 repository에 넣을수 있으면 그렇게 하겠습니다!!
* */

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AlbumCheckAccessServiceImpl implements AlbumCheckAccessService{
    private final BlockRepository blockRepository;
    private final FriendRepository friendRepository;
    @Override
    public void checkAccess(Album album, Long currentUserId) {
        // 차단 여부 확인
        if (blockRepository.existsByMember_IdAndTarget_Id(album.getMember().getId(), currentUserId)) {
            throw new GeneralException(ErrorStatus.ALBUM_ACCESS_RESTRICTED);
        }

        // 공개 범위에 따른 접근 권한 확인
        switch (album.getScope()) {
            case MY:
                if (!album.getMember().getId().equals(currentUserId)) {
                    throw new GeneralException(ErrorStatus.ALBUM_ACCESS_RESTRICTED);
                }
                break;
            case FRIEND:
                if (!friendRepository.isFriend(currentUserId, album.getMember().getId())) {
                    throw new GeneralException(ErrorStatus.ALBUM_ACCESS_RESTRICTED);
                }
                break;
            case ALL:
                // 전체 공개이므로 추가 검증 필요 없음
                break;
            default:
                throw new GeneralException(ErrorStatus.ALBUM_SCOPE_NOT_FOUND);
        }
    }

    @Override
    public boolean checkAccessInBool(Album album, Long currentUserId) {
        // 차단 여부 확인
        if (blockRepository.existsByMember_IdAndTarget_Id(album.getMember().getId(), currentUserId)) {
           return false;
        }

        // 공개 범위에 따른 접근 권한 확인
        switch (album.getScope()) {
            case MY:
                if (!album.getMember().getId().equals(currentUserId)) {
                    return false;
                }
                break;
            case FRIEND:
                if (!friendRepository.isFriend(currentUserId, album.getMember().getId())) {
                    return false;
                }
                break;
            case ALL:
                // 전체 공개이므로 추가 검증 필요 없음
                return true;
            default:
                return true;
        }
        return true;
    }
}
