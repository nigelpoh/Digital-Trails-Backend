package types.weather_forecast_4days_api;

import com.fasterxml.jackson.annotation.JsonProperty; 

public class Forecast {
  private Temperature temperature;
  private String date;
  private String forecast;
  @JsonProperty("relative_humidity")
  private Humidity relativeHumidity;
  private Wind wind;
  private String timestamp;

  public Temperature getTemperature() {
      return temperature;
  }

  public String getDate() {
      return date;
  }

  public String getForecast() {
      return forecast;
  }

  public Humidity getRelativeHumidity() {
      return relativeHumidity;
  }

  public Wind getWind() {
      return wind;
  }

  public String getTimestamp() {
      return timestamp;
  }
}
