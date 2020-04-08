package com.project.air_quality;



import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;

import java.util.List;

import static jdk.nashorn.internal.objects.NativeNumber.valueOf;

// api can convert location to coordinates
//https://developer.here.com/documentation/geocoding-search-api/dev_guide/topics/get-credentials-ols.html
//https://www.iqair.com/dashboard/api

//documentation
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

    @RequestMapping(path="/getCity", method = RequestMethod.POST)
    @ResponseBody
    public City getCity(String chosen_state ) throws ParseException{

        if (this.airQualityService.exists(chosen_state)){
            System.out.println(chosen_state + " encontra-se na cache!");
            return this.airQualityService.getAirQuality(chosen_state);
        }

        String url = this.HOST_A + "city?city=" + chosen_state + "&state=" + chosen_state +
                        "&country=Portugal&key=" + this.API_KEY_A;

        JSONObject data = this.requestApi(url);
        data = (JSONObject) data.get("data");
        data = (JSONObject) data.get("location");
        JSONArray coords = (JSONArray) data.get("coordinates");
        double lat =  valueOf(coords.get(1));
        double lon =  valueOf(coords.get(0));

        url = this.HOST_B + "lat=" + lat + "&lon=" + lon + "&key=" + this.API_KEY_B;
        data = this.requestApi(url);
        data = (JSONObject) data.get("data");
        data = (JSONObject) data.get("indexes");
        data = (JSONObject) data.get("baqi"); // have aqi, category and dominant_pollutant


        return this.airQualityService.save(new City(chosen_state, lat, lon,
                                            (long) data.get("aqi"),
                                            (String) data.get("category"),
                                            (String) data.get("dominant_pollutant")));
    }

    @RequestMapping(path="/airquality", method = RequestMethod.POST)
    public String airQuality(Model model, @RequestParam(name = "chosen_state") String chosen_state) throws ParseException{
        City new_city = this.getCity(chosen_state);
        model.addAttribute("city", new_city);
        return "baqi";
    }


    @RequestMapping(path="/getCities", method = RequestMethod.GET)
    @ResponseBody
    public List<City> getAllCities(){
        return this.airQualityService.getAllCitiesSave();
    }
}
