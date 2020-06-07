package com.microsoft.sfe.controller;

import com.microsoft.sfe.dto.FoodTruckDTO;
import com.microsoft.sfe.service.FoodTruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodTruckController {

    private static final Logger logger  = LoggerFactory.getLogger(FoodTruckController.class);
    @Autowired
    private FoodTruckService foodTruckService;

    /**
     * This method retrieves nearest food truck options based on user's location and radius
     * @param latitude - latitude of user location
     * @param longitude - longitude of user location
     * @param radius - radius to confine results in
     * @return List<foodTruckDTO> - List of food trucks with details
     */
    @GetMapping(value="/food-trucks/location/{latitude}/{longitude}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodTruckDTO>> getFoodTrucks(@PathVariable("latitude") double latitude,
                                                            @PathVariable("longitude") double longitude,
                                                            @RequestParam("radius") Optional<Double> radius) {
        HttpStatus httpStatus = HttpStatus.OK;
        List<FoodTruckDTO> foodTruckDTOs = new ArrayList<>();
        try {
            foodTruckDTOs = foodTruckService.getFoodTruckLists(latitude, longitude, radius.orElse(0.0));
        }
        catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error("There was an error retrieving food trucks near you" + e.toString());
        }
        return new ResponseEntity(foodTruckDTOs, httpStatus);
    }
}
