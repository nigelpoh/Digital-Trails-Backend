package helpers.weather;

import types.weather_forecast_2h_api.Area;
import types.weather_forecast_2h_api.Forecast;
import types.weather_forecast_2h_api.Forecast2hWeatherResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Exception;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import helpers.misc.Miscellaneous;

public class TwoHourForecast {
  public Forecast getCurrentWeather(double lat, double lng) {
    Area chosenArea = null;
    double chosenDistance = Double.MAX_VALUE;
    Forecast2hWeatherResponse response = null;
    for (int chance = 1; chance < 4; chance++) {
      String url;
      if(chance != 3){
        LocalDateTime timeNow = LocalDateTime.now();
        timeNow = timeNow.withSecond(0).withNano(0).minusMinutes(timeNow.getMinute() % (30 * chance));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        url = UriComponentsBuilder.fromUriString("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast")
              .queryParam("date_time", timeNow.format(formatter))
              .build(true)
              .toUri()
              .toString();
      }else{
        url = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date=2023-04-06";
      }
      RestTemplate restTemplate = new RestTemplate();
      response = restTemplate.getForObject(url, Forecast2hWeatherResponse.class);
      try{
        if(response.getAreas().size() == 0){
          throw new Exception("No results");
        }
        System.out.println("No error (2h)");
      } catch (Exception e){
        System.out.println("Error (2h)");
        continue;
      }
      for (Area area : response.getAreas()) {
        if(chosenArea == null) {
          chosenArea = area;
        }else{
          double distance = Miscellaneous.getDistanceKM(lat, lng, area.getLocation().getLatitude(), area.getLocation().getLongitude());
          if (distance < chosenDistance) {
            chosenArea = area;
            chosenDistance = distance;
          }
        }
      }
      if(chosenDistance < 10){
        break;
      }
    }
    for (Forecast forecast : response.getItems().get(0).getForecasts()) {
      if (forecast.getArea().equals(chosenArea.getName())) {
        return forecast;
      }
    }
    return null;
  }
}
