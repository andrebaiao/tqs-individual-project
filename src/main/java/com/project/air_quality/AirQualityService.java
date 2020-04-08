package com.project.air_quality;

import java.util.List;

public interface AirQualityService {
    City getAirQuality(String name);
    boolean exists(String name);
    City save(City city);
    List<City> getAllCitiesSave();
}
