package org.example.petwif.service.BlockService;

import org.example.petwif.domain.entity.Block;
import org.springframework.data.domain.Slice;

public interface BlockQueryService {

    boolean getBlockStatus(Long memberId, Long targetId);
    Slice<Block> getBlockList(Long memberId, Integer page);
}
