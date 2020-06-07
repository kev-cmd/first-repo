package com.microsoft.sfe.service;

import com.microsoft.sfe.dto.FoodTruckDTO;

import java.util.List;

public interface FoodTruckService {
    List<FoodTruckDTO> getFoodTruckLists(double latitude, double longitude, double radius);
}
