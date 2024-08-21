package org.example.petwif.domain.enums;

public enum AlbumSortType {
    LIKES("likeCount"),
    COMMENTS("commentCount"),
    BOOKMARKS("bookmarkCount"),
    LATEST("updatedAt");

    private final String sortField;

    AlbumSortType(String sortField) {
        this.sortField = sortField;
    }

    public String getSortField() {
        return sortField;
    }
}
