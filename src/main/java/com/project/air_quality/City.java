package com.project.air_quality;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 3, max = 20)
    private String name;
    private double lat;
    private double lng;

    @Size(min = 0, max = 100)
    private double air_quality;
    private String category;

    public City(String name, double lat, double lng){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(double air_quality) {
        this.air_quality = air_quality;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
