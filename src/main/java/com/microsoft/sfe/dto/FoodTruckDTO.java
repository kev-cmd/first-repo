package com.microsoft.sfe.dto;

import lombok.Data;

@Data
public class FoodTruckDTO {
    private String id;
    private String name;
    private String facilityType;
    private String foodItems;
    private String address;
    private String block;
    private String lot;
    private String locationDesc;
    private double distance;
}
