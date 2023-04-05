package helpers.weather;

import types.weather_api.Forecast;
import types.weather_forecast_24h_api.Forecast24hWeatherResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class TwentyFourHourForecast {
  public Forecast getWeatherForecast(double lat, double lng) {
    LocalDateTime timeNow = LocalDateTime.now();
    timeNow = timeNow.withSecond(0).withNano(0).minusMinutes(timeNow.getMinute() % (30));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    String url = UriComponentsBuilder.fromUriString("https://api.data.gov.sg/v1/environment/24-hour-weather-forecast")
          .queryParam("date_time", timeNow.format(formatter))
          .build(true)
          .toUri()
          .toString();
    RestTemplate restTemplate = new RestTemplate();
    Forecast24hWeatherResponse response = restTemplate.getForObject(url, Forecast24hWeatherResponse.class);
    Forecast tomorrow = new Forecast();
    if(response.getItems().get(0).getGeneral().getForecast() != null && response.getItems().get(0).getGeneral().getTemperature() != null){
      double midrangetemp = (response.getItems().get(0).getGeneral().getTemperature().getHigh() + response.getItems().get(0).getGeneral().getTemperature().getLow()) / 2;
      tomorrow.setTemperature(midrangetemp);
      tomorrow.setWeather(response.getItems().get(0).getGeneral().getForecast());
      return tomorrow;
    }
    return tomorrow;
  }
}