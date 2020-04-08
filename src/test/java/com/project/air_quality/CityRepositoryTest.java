package com.project.air_quality;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void whenFindByName_thenReturnCity(){
        City braga = new City("Braga", 48.12, -2.0, 48, "Great", "om2");
        entityManager.persistAndFlush(braga);

        City found = cityRepository.findByName(braga.getName());
        assertThat(found.getName()).isEqualTo(braga.getName());
    }

    @Test
    public void whenInvalidName_thenReturnNull(){
        City fromDb = cityRepository.findByName("doesNotExist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindById_thenReturnNull() {
        City braga = new City("Braga", 48.12, -2.0, 48, "Great", "om2");
        entityManager.persistAndFlush(braga);

        City found = cityRepository.findById(braga.getId()).orElse(null);
        assertThat(found.getName()).isEqualTo(braga.getName());
    }

    @Test
    public void whenInvalidId_thenReturnNull(){
        City fromDb = cityRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfCities_whenFindAll_thenReturnAllCities(){
        City braga = new City("Braga", 48.12, -2.0, 48, "Great", "om2");
        City aveiro = new City("Aveiro", 48.12, -2.0, 48, "Great", "om2");
        City lisboa = new City("Lisboa", 48.12, -2.0, 48, "Great", "om2");
        City acores = new City("Açores", 48.12, -2.0, 48, "Great", "om2");

        entityManager.persist(braga);
        entityManager.persist(aveiro);
        entityManager.persist(lisboa);
        entityManager.persist(acores);

        List<City> allCities = cityRepository.findAll();

        assertThat(allCities).hasSize(4).extracting(City::getName).containsOnly(braga.getName(), aveiro.getName(),
                                                                    lisboa.getName(), acores.getName());
    }
}
