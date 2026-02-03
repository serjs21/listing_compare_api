package com.listings_compare_api.dto;

import java.util.List;

public record GoogleSearchResponseDTO(List<PlaceDTO> places) {
    public record PlaceDTO(String id, String formattedAddress, DisplayNameDTO displayName) {
    }

    public record DisplayNameDTO(String text, String languageCode) {
    }
}
