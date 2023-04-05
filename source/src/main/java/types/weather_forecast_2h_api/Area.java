package types.weather_forecast_2h_api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {
  private String name;
  @JsonProperty("label_location")
  private Location labelLocation;

  public String getName() {
    return name;
  }

  public Location getLocation() {
    return labelLocation;
  }
}
