package com.project.air_quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AirQualityServiceImpl implements AirQualityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City getAirQuality(String name) {
        return this.cityRepository.findByName(name);
    }

    @Override
    public boolean exists(String name) {
        return this.cityRepository.findByName(name) != null;
    }

    @Override
    public City save(City city) {
        return this.cityRepository.save(city);
    }

    @Override
    public List<City> getAllCitiesSave() {
        return this.cityRepository.findAll();
    }
}
