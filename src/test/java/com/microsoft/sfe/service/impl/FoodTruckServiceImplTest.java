package com.microsoft.sfe.service.impl;

import com.microsoft.sfe.dto.FoodTruckDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FoodTruckServiceImplTest {

    @InjectMocks
    FoodTruckServiceImpl foodTruckService = new FoodTruckServiceImpl();

    /* Constants */
    private static final double LAT1 = 37.7806943774082;
    private static final double LONG1 = -122.409668813219;
    private static final double LAT2 = 37.7275665375917;
    private static final double LONG2 = -122.432969701989;
    private static final double RADIUS = 1.0;

    @Test
    public void getFoodTrucks_withRadius() {
        List<FoodTruckDTO> foodTrucks = foodTruckService.getFoodTruckLists(LAT1, LONG1, RADIUS);
        assertFalse(isEmpty(foodTrucks));
        assertThat(foodTrucks.size(),greaterThan(5));
    }

    @Test
    public void getFoodTrucks_withoutRadius() {
        List<FoodTruckDTO> foodTrucks = foodTruckService.getFoodTruckLists(LAT1, LONG1, 0.0);
        assertFalse(isEmpty(foodTrucks));
        assertThat(foodTrucks.size(),greaterThan(5));
    }

    @Test
    public void calculateDistance_sameCoordinates() {
        double dist = foodTruckService.calculateDistance(LAT1, LONG1, LAT1, LONG1);
        assertEquals(0.0, dist);
    }

    @Test
    public void calculateDistance_differentCoordinates() {
        double dist = foodTruckService.calculateDistance(LAT1, LONG1, LAT2, LONG2);
        assertThat(dist, greaterThan(0.0));
    }
}
