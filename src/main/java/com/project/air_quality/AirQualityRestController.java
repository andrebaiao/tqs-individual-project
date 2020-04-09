package com.project.air_quality;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.NativeNumber.valueOf;

// documentation apis:
// https://api-docs.airvisual.com/?version=latest
// https://docs.breezometer.com/api-documentation/air-quality-api/v2/#examples

@Controller
@RequestMapping("/api")
public class AirQualityRestController {
    private JSONParser parser = new JSONParser();
    private final String API_KEY_A = "092f923d-179b-4ce6-b8ea-beee3c882c71";
    private final String HOST_A = "https://api.airvisual.com/v2/";
    //example url:
    //https://api.breezometer.com/air-quality/v2/current-conditions?lat={latitude}&lon={longitude}&key=YOUR_API_KEY
    private final String HOST_B = "https://api.breezometer.com/air-quality/v2/current-conditions?";
    private final String API_KEY_B = "d77c130eed884f7c85c34247a174eadc";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AirQualityService airQualityService;

    public JSONObject requestApi(String url) throws ParseException {
        String response = restTemplate.getForObject(url, String.class);
        return (JSONObject) parser.parse(response);
    }

    @RequestMapping(method = RequestMethod.GET, value="/airquality")
    public String start(Model model) throws ParseException{
        String url = this.HOST_A + "states?country=Portugal&key=" + this.API_KEY_A;
        JSONObject result = this.requestApi(url);
        JSONArray data = (JSONArray) result.get("data");

        List<String> states = new ArrayList<>();

        for(Object s: data){
            JSONObject state = (JSONObject) s;
            states.add((String) state.get("state"));
        }
        model.addAttribute("states",states);
        return "index";
    }

    @RequestMapping(path="/getCity", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<City> getCity(@RequestParam String city_name) throws ParseException, InterruptedException {

        // Simulation time to live.
        // If city is in cache and their time of request when entry in cache as bigger than this time plus
        // one minute, need to be remove.
        Calendar target_date = Calendar.getInstance();
        if (this.airQualityService.exists(city_name)){

            City target_city = this.airQualityService.getAirQuality(city_name);
            Calendar requestDateTTL = target_city.getTimeOfRequest();
            requestDateTTL.add(Calendar.MINUTE, 1);

            if(requestDateTTL.compareTo(target_date) < 0) {
                this.airQualityService.deleteCity(target_city);
            } else {
                System.out.println(city_name + " encontra-se na cache!");

                this.airQualityService.countHits();
                return new ResponseEntity<>(target_city, HttpStatus.OK);
            }
        }

        String url = this.HOST_A + "city?city=" + city_name + "&state=" + city_name +
                "&country=Portugal&key=" + this.API_KEY_A;;
        JSONObject data;

        try {
            data = this.requestApi(url);
        }
        catch (Exception e){
            return new ResponseEntity<>(new City(), HttpStatus.NOT_FOUND);
        }

        data = (JSONObject) data.get("data");
        data = (JSONObject) data.get("location");
        JSONArray coords = (JSONArray) data.get("coordinates");
        double lat =  valueOf(coords.get(1));
        double lon =  valueOf(coords.get(0));

        url = this.HOST_B + "lat=" + lat + "&lon=" + lon + "&key=" + this.API_KEY_B;

        try{
            data = this.requestApi(url);
        }catch (Exception e){
            return new ResponseEntity<>(new City(), HttpStatus.NOT_FOUND);
        }

        data = (JSONObject) data.get("data");
        data = (JSONObject) data.get("indexes");
        data = (JSONObject) data.get("baqi"); // have aqi, category and dominant_pollutant

        City new_city = this.airQualityService.save(new City(city_name, lat, lon,
                (long) data.get("aqi"),
                (String) data.get("category"),
                (String) data.get("dominant_pollutant")));

        this.airQualityService.countMisses();

        return new ResponseEntity<>(new_city, HttpStatus.OK);
    }

    @RequestMapping(path="/airquality", method = RequestMethod.POST)
    public String airQuality(Model model, @RequestParam(name = "chosen_state") String chosen_state) throws ParseException, InterruptedException {
        ResponseEntity<City> response = this.getCity(chosen_state);
        HttpStatus statuscode = response.getStatusCode();

        if (statuscode == HttpStatus.NOT_FOUND)
            model.addAttribute("error", "Data unavailable!");

        City new_city = response.getBody();
        model.addAttribute("city", new_city);

        return "baqi";
    }

    @RequestMapping(path="/getCities", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<City>> getAllCities(){
        return new ResponseEntity<>(this.airQualityService.getAllCitiesSave(), HttpStatus.OK);
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Integer> getStatistics(){
        return this.airQualityService.getStatistic();
    }

}
