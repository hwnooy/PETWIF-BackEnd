package org.example.petwif.service.BlockService;

import org.example.petwif.domain.entity.Block;

public interface BlockCommandService {
    Block doBlock(Long memberId, Long targetId);
    void deleteBlock(Long memberId, Long targetId);
}

