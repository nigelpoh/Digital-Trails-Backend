package types.weather_forecast_24h_api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

  @JsonProperty("update_timestamp")
  private String updateTimestamp;
  private String timestamp;
  @JsonProperty("valid_period")
  private ValidPeriod validPeriod;
  private General general;
  private List<Period> periods;

  public String getUpdateTimestamp() {
      return updateTimestamp;
  }

  public String getTimestamp() {
      return timestamp;
  }

  public ValidPeriod getValidPeriod() {
      return validPeriod;
  }

  public General getGeneral() {
      return general;
  }

  public List<Period> getPeriods() {
      return periods;
  }
}