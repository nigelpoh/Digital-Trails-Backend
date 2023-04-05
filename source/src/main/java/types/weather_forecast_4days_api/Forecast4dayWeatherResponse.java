package types.weather_forecast_4days_api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Forecast4dayWeatherResponse {
  private List<Item> items;
  @JsonProperty("api_info")
  private StatusInfo statusInfo;

  public List<Item> getItems() {
      return items;
  }

  public StatusInfo getStatusInfo() {
      return statusInfo;
  }
}
