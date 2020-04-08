package com.project.air_quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static com.sun.deploy.perf.DeployPerfUtil.put;

@Service
public class AirQualityServiceImpl implements AirQualityService {

    @Autowired
    private CityRepository cityRepository;

    private int hits = 0;
    private int misses = 0;

    @Override
    public City getAirQuality(String name) {
        return this.cityRepository.findByName(name);
    }

    @Override
    public City getCityById(Long id) {
        return this.cityRepository.findById(id).orElse(null);
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

    @Override
    public void countHits() {
        this.hits ++;
    }

    @Override
    public void countMisses() {
        this.misses ++;
    }




    @Override
    public HashMap<String, Integer> getStatistic() {
        int misses = this.misses;
        int hits = this.hits;
        return new HashMap<String, Integer>(){{
            put("Number of requests: ", misses + hits);
            put("Number of hits: ", hits);
            put("Number of misses: ", misses);
        }};
    }
}
