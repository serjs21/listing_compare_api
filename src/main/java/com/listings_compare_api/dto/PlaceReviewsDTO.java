package com.listings_compare_api.dto;

import java.util.List;

public record PlaceReviewsDTO(
    String id, Double rating, Integer userRatingCount, List<ReviewDTO> reviews) {}
