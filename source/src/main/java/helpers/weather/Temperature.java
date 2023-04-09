package helpers.weather;

import types.temperature_realtime_api.Station;
import types.temperature_realtime_api.Reading;
import types.temperature_realtime_api.RealtimeWeatherResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Exception;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import helpers.misc.Miscellaneous;

public class Temperature {
  public double getCurrentTemperature(double lat, double lng) {
    Station chosenStation = null;
    double chosenDistance = Double.MAX_VALUE;
    RealtimeWeatherResponse response = null;
    for (int chance = 1; chance < 4; chance++) {
      String url;
      if(chance != 3){
        LocalDateTime timeNow = LocalDateTime.now();
        timeNow = timeNow.withSecond(0).withNano(0).minusMinutes(timeNow.getMinute() % (30 * chance));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println(timeNow.format(formatter));
        url = UriComponentsBuilder.fromUriString("https://api.data.gov.sg/v1/environment/air-temperature")
              .queryParam("date_time", timeNow.format(formatter))
              .build(true)
              .toUri()
              .toString();
      }else{
        url = "https://api.data.gov.sg/v1/environment/air-temperature?date=2023-04-09";
      }
      RestTemplate restTemplate = new RestTemplate();
      response = restTemplate.getForObject(url, RealtimeWeatherResponse.class);
      try{
        if(response.getMetadata().getStations().length == 0){
          throw new Exception("No results");
        }
        System.out.println("No error (Temperature)");
      } catch (Exception e){
        System.out.println("Error (Temperature)");
        continue;
      }
      for (Station station : response.getMetadata().getStations()) {
        if(chosenStation == null) {
          chosenStation = station;
        }else{
          double distance = Miscellaneous.getDistanceKM(lat, lng, station.getLocation().getLatitude(), station.getLocation().getLongitude());
          if (distance < chosenDistance) {
            chosenStation = station;
            chosenDistance = distance;
          }
        }
      }
      if(chosenDistance < 10){
        break;
      }
    }
    for (Reading reading : response.getItems().get(0).getReadings()) {
      if (reading.getStationId().equals(chosenStation.getId())) {
        return reading.getValue();
      }
    }
    return -1;
  }
}
