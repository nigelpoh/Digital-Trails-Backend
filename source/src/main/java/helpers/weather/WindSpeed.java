package helpers.weather;

import types.temperature_realtime_api.Station;
import types.temperature_realtime_api.Reading;
import types.temperature_realtime_api.RealtimeWeatherResponse;

import helpers.misc.Miscellaneous;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class WindSpeed {
  public double getCurrentWindSpeed(double lat, double lng){
    Station chosenStation = null;
    double chosenDistance = Double.MAX_VALUE;
    RealtimeWeatherResponse response = null;
    for (int chance = 1; chance < 7; chance++) {
      LocalDateTime timeNow = LocalDateTime.now();
      timeNow = timeNow.withSecond(0).withNano(0).minusMinutes(timeNow.getMinute() % (5 * chance));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      System.out.println(timeNow.format(formatter));
      String url = UriComponentsBuilder.fromUriString("https://api.data.gov.sg/v1/environment/wind-speed")
            .queryParam("date_time", timeNow.format(formatter))
            .build(true)
            .toUri()
            .toString();
      RestTemplate restTemplate = new RestTemplate();
      response = restTemplate.getForObject(url, RealtimeWeatherResponse.class);
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
    System.out.println("Chosen station: " + chosenStation.getId() + " " + chosenStation.getName());
    for (Reading reading : response.getItems().get(0).getReadings()) {
      if (reading.getStationId().equals(chosenStation.getId())) {
        return reading.getValue();
      }
    }
    return -1;
  }
}
