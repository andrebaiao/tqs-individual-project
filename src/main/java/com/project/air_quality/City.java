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

    private long air_quality;
    private String category;
    private String dominant_pollutant;

    public City() {
    }

    public City(String name, double lat, double lng, long air_quality,
                String category, String dominant_pollutant){

        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.air_quality = air_quality;
        this.category = category;
        this.dominant_pollutant = dominant_pollutant;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat){
            this.lat = lat;
        }

    public double getLng() {
            return this.lng;
        }

    public void setLng(double lng) {
      this.lng = lng;
    }

    public double getAir_quality() {
        return this.air_quality;
    }

    public void setAir_quality(long air_quality) {
        this.air_quality = air_quality;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDominant_pollutant(String dominant_pollutant){
        this.dominant_pollutant = dominant_pollutant;
    }

    public String getDominant_pollutant(){
        return this.dominant_pollutant;
    }
}
