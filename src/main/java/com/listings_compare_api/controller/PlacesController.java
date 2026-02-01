package com.listings_compare_api.controller;
import com.listings_compare_api.dto.SearchPlaceByNameDTO;
import com.listings_compare_api.service.GooglePlacesService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
public class PlacesController {
    private final GooglePlacesService googlePlacesService;

    public PlacesController(GooglePlacesService googlePlacesService) {
        this.googlePlacesService = googlePlacesService;
    }

    @GetMapping("/ping")
    public String ping() {
        return GooglePlacesService.ping();
    }
    
    @PostMapping("/search")
    public Object search(@RequestBody SearchPlaceByNameDTO searchBody) {
        return this.googlePlacesService.getPlacesData(searchBody.name());
    }
}