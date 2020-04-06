package com.project.air_quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AirQualityServiceImpl implements AirQualityService {

    @Override
    public City getAirQuality(double lat, double lon) {
        return null;
    }
}
