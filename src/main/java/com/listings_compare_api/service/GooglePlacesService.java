package com.listings_compare_api.service;
import com.listings_compare_api.dto.PlacesDTO;

import org.springframework.stereotype.Service;

@Service
public class GooglePlacesService {
    public static String ping() {
        return "ça marche";
    }

    public PlacesDTO getPlacesData(String name) {
        return new PlacesDTO("Test Place", "Test place id");
    }
}
