package com.microsoft.sfe.service.impl;

import com.microsoft.sfe.config.ConfigUtil;
import com.microsoft.sfe.dto.FoodTruckDTO;
import com.microsoft.sfe.service.FoodTruckService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodTruckServiceImpl implements FoodTruckService {

    private static final Logger logger  = LoggerFactory.getLogger(FoodTruckServiceImpl.class);
    /**
     * Gets list of food trucks based on location and radius
     * @param lat1 - latitude of user location
     * @param long1 - longitude of user location
     * @param radius - radius to confine results in
     * @return List<foodTruckDTO> - List of food truck options with details
     */
    @Override
    public List<FoodTruckDTO> getFoodTruckLists(double lat1, double long1, double radius) {
        String cvsSplitBy = ",";
        String line;
        List<FoodTruckDTO> foodTrucks = new ArrayList<>();

        // create the url
        URL url = null;
        try {
            url = new URL(ConfigUtil.DATASF_URL);
        } catch (MalformedURLException e) {
            logger.error("Error creating URL for DATASF"+e);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            // Skip the first header line
            br.readLine();
            double lat2 = 0;
            double long2 = 0;
            double distance;

            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] foodTruckDetails = line.split(cvsSplitBy);

                if (foodTruckDetails[14] != null && foodTruckDetails[15] != null
                        && foodTruckDetails[14].length() > 0 && foodTruckDetails[15].length() > 0) {
                    try {
                        lat2 = Double.parseDouble(foodTruckDetails[14]);
                        long2 = Double.parseDouble(foodTruckDetails[15]);
                    } catch(NumberFormatException e) {
                        logger.error("Error reading food truck coordinates" + e.toString());
                    }
                }
                distance = calculateDistance(lat1, long1, lat2, long2);
                FoodTruckDTO foodTruckDTO = setFoodTruckDTO(foodTruckDetails, distance);
                foodTrucks.add(foodTruckDTO);
            }
            //Sort the list by distance
            foodTrucks.sort(Comparator.comparing(FoodTruckDTO::getDistance));

            //Filter food trucks within vicinity of given radius
            List<FoodTruckDTO> foodTrucksInVicinity;
            if (radius > 0) {
                foodTrucksInVicinity = foodTrucks.stream().
                        filter(foodTruck -> foodTruck.getDistance()<=radius).collect(Collectors.toList());
            } else {
                //TODO : possibly limit return records when no radius provided
                return foodTrucks;
            }

            //Keep 5 closest options if less than 5 food trucks found within given radius
            if (foodTrucksInVicinity.size()<5) {
                foodTrucksInVicinity = foodTrucks.stream().limit(5).collect(Collectors.toList());
            }
            foodTrucks = foodTrucksInVicinity;
        } catch (IOException e) {
            logger.error("Error retrieving food truck lists from csv" + e.toString());
        } catch (Exception e) {
            logger.error("Error retrieving food truck lists" + e.toString());
        }
        return foodTrucks;
    }

    /**
     * THis helper method calculates distance between user and food truck locations
     * @param lat1 - user location latitude
     * @param long1 - user location langitude
     * @param lat2 - food truck location lattitude
     * @param long2 - food truck location longitude
     * @return distance - distance between user and food truck locations
     */
    public double calculateDistance(double lat1, double long1, double lat2, double long2) {
        if ((lat1 == lat2) && (long1 == long2)) {
            return 0;
        }
        else {
            try {
                double theta = long1 - long2;
                double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
                dist = Math.acos(dist);
                dist = Math.toDegrees(dist);
                dist = dist * 60 * 1.1515;
                return dist;
            } catch (Exception e) {
                logger.error("Error calculating distance" + e.toString());
                return -1;
            }
        }
    }

    /**
     * This helper method sets foodTruckDTO with food truck details
     * @param foodTruckDetails food truck metadata
     * @param distance distance to food truck from user location
     * @return foodTruckDTO
     */
    private FoodTruckDTO setFoodTruckDTO(String[] foodTruckDetails, double distance) {
        FoodTruckDTO foodTruckDTO = new FoodTruckDTO();
        foodTruckDTO.setId(Objects.toString(foodTruckDetails[0], ""));
        foodTruckDTO.setName(Objects.toString(foodTruckDetails[1], ""));
        foodTruckDTO.setFacilityType(Objects.toString(foodTruckDetails[2],""));
        foodTruckDTO.setAddress(Objects.toString(foodTruckDetails[5],""));
        foodTruckDTO.setFoodItems(Objects.toString(foodTruckDetails[11],""));
        foodTruckDTO.setBlock(Objects.toString(foodTruckDetails[7],""));
        foodTruckDTO.setLot(Objects.toString(foodTruckDetails[8],""));
        foodTruckDTO.setLocationDesc(Objects.toString(foodTruckDetails[4],""));
        foodTruckDTO.setDistance(distance);
        return foodTruckDTO;
    }
}
