package types.weather_forecast_24h_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Forecast24hWeatherResponse {
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