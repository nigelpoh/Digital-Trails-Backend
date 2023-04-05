package helpers.weather;

import types.weather_api.Forecast;
import types.weather_forecast_4days_api.Forecast4dayWeatherResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FourDaysForecast {
  public List<Forecast> getWeatherForecast(double lat, double lng) {
    LocalDateTime timeNow = LocalDateTime.now();
    timeNow = timeNow.withSecond(0).withNano(0).minusMinutes(timeNow.getMinute() % (30));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    String url = UriComponentsBuilder.fromUriString("https://api.data.gov.sg/v1/environment/4-day-weather-forecast")
          .queryParam("date_time", timeNow.format(formatter))
          .build(true)
          .toUri()
          .toString();
    RestTemplate restTemplate = new RestTemplate();
    Forecast4dayWeatherResponse response = restTemplate.getForObject(url, Forecast4dayWeatherResponse.class);
    List<Forecast> responseFinal = new ArrayList<Forecast>();
    for (int i = 0; i < response.getItems().get(0).getForecasts().size(); i++) {
      double midrangetemp = (response.getItems().get(0).getForecasts().get(i).getTemperature().getHigh() + response.getItems().get(0).getForecasts().get(i).getTemperature().getLow()) / 2;
      Forecast responsePart = new Forecast();
      responsePart.setWeather(response.getItems().get(0).getForecasts().get(i).getForecast());
      responsePart.setTemperature(midrangetemp);
      responseFinal.add(responsePart);
    }
    return responseFinal;
  }
}