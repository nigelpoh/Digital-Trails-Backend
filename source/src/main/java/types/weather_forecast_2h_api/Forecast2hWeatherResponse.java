package types.weather_forecast_2h_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Forecast2hWeatherResponse {

  @JsonProperty("area_metadata")
  private List<Area> areaMetadata;
  private List<Item> items;
  @JsonProperty("api_info")
  private StatusInfo statusInfo;

  public List<Area> getAreas() {
    return areaMetadata;
  }

  public List<Item> getItems() {
    return items;
  }

  public StatusInfo getStatusInfo() {
    return statusInfo;
  }
}