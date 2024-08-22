package org.example.petwif.service.albumService;

import org.example.petwif.domain.entity.Album;

public interface AlbumCheckAccessService {
    public void checkAccess(Album album, Long currentUserId);

    public boolean checkAccessInBool(Album album, Long currentUserId);

    public boolean checkAccessInBoolForSearch(Album album, Long currentUserId);
}
