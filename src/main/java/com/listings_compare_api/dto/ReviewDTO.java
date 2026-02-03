package com.listings_compare_api.dto;

public record ReviewDTO(
    String name,
    int rating,
    String text,
    String languageCode,
    AuthorAttribution authorAttribution,
    String publishTime
) {
    public record AuthorAttribution(
        String displayName,
        String uri,
        String photoUri
    ) {}
}
