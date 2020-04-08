package com.project.air_quality;

import java.util.HashMap;
import java.util.List;

public interface AirQualityService {
    City getAirQuality(String name);
    City getCityById(Long id);
    boolean exists(String name);
    City save(City city);
    List<City> getAllCitiesSave();
    void countHits();
    void countMisses();
    HashMap<String, Integer> getStatistic();
}
