package com.project.air_quality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceImplTest {

    @Mock( lenient = true)
    private CityRepository cityRepository;

    @InjectMocks
    private AirQualityServiceImpl airQualityService;

    @BeforeEach
    public void setUp() {
        City braga = new City("Braga", 48.12, -2.0, 48, "Great", "om2");
        City lisboa = new City("Lisboa", 48.12, -2.0, 48, "Great", "om2");
        City aveiro = new City("Aveiro", 48.12, -2.0, 48, "Great", "om2");

        List<City> allCities = Arrays.asList(braga, lisboa,aveiro);

        airQualityService.setMisses(3);

        Mockito.when(cityRepository.findByName(braga.getName())).thenReturn(braga);
        Mockito.when(cityRepository.findByName(lisboa.getName())).thenReturn(lisboa);
        Mockito.when(cityRepository.findByName(aveiro.getName())).thenReturn(aveiro);
        Mockito.when(cityRepository.findByName("wrong_name")).thenReturn(null);
        Mockito.when(cityRepository.findById(braga.getId())).thenReturn(Optional.of(braga));
        Mockito.when(cityRepository.findAll()).thenReturn(allCities);
        Mockito.when(cityRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
    public void whenValidName_thenCityShouldBeFound(){
        String name = "Braga";
        City found = airQualityService.getAirQuality(name);

        assertThat(found.getName()).isEqualTo(name);
        verifyFindByNameIsCalledOnce("Braga");
    }

    @Test
    public void whenInvalidName_thenCityShouldNotBeFound(){
        City fromDb = airQualityService.getAirQuality("wrong city");
        assertThat(fromDb).isNull();

        verifyFindByNameIsCalledOnce("wrong city");
    }

    @Test
    public void whenValidName_thenCityShouldExist(){
        boolean doesCityExist = airQualityService.exists("Aveiro");
        assertThat(doesCityExist).isEqualTo(true);

        verifyFindByNameIsCalledOnce("Aveiro");
    }

    @Test
    public void whenNonExistingName_theCityShouldNotExist(){
        boolean doesCityExist = airQualityService.exists("dont exist");
        assertThat(doesCityExist).isEqualTo(false);

        verifyFindByNameIsCalledOnce("dont exist");
    }

    @Test
    public void whenValidId_thenCityShouldBeFound() {
        City fromDb = airQualityService.getCityById(cityRepository.findByName("Braga").getId());
        assertThat(fromDb.getName()).isEqualTo("Braga");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenCityShouldNotBeFound() {
        City fromDb = airQualityService.getCityById(-99L);
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    @Test
    public void given3City_whengetAll_thenReturn3Records() {
        City braga = new City("Braga", 48.12, -2.0, 48, "Great", "om2");
        City lisboa = new City("Lisboa", 48.12, -2.0, 48, "Great", "om2");
        City aveiro = new City("Aveiro", 48.12, -2.0, 48, "Great", "om2");

        List<City> allCities = airQualityService.getAllCitiesSave();
        verifyFindAllCitiesIsCalledOnce();
        assertThat(allCities).hasSize(3).extracting(City::getName).contains(braga.getName(), lisboa.getName(), aveiro.getName());
    }

    @Test
    public void whenGetStatistics_thenReturnHasMap() {
        HashMap<String, Integer> statistics =  airQualityService.getStatistic();

        assertThat(statistics.get("Number of requests: ")).isEqualTo(3);
        assertThat(statistics.get("Number of hits: ")).isEqualTo(0);
        assertThat(statistics.get("Number of misses: ")).isEqualTo(3);
    }


    private void verifyFindByNameIsCalledOnce(String name_city) {
        Mockito.verify(cityRepository, VerificationModeFactory.times(1)).findByName(name_city);
        Mockito.reset(cityRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(cityRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(cityRepository);
    }

    private void verifyFindAllCitiesIsCalledOnce() {
        Mockito.verify(cityRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(cityRepository);
    }


}
