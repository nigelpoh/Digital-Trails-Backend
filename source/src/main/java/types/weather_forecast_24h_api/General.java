package types.weather_forecast_24h_api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class General {

  private String forecast;
  @JsonProperty("relative_humidity")
  private RelativeHumidity relativeHumidity;
  private Temperature temperature;
  private Wind wind;

  public String getForecast() {
      return forecast;
  }

  public RelativeHumidity getRelativeHumidity() {
      return relativeHumidity;
  }

  public Temperature getTemperature() {
      return temperature;
  }

  public Wind getWind() {
      return wind;
  }
}