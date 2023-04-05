package types.weather_forecast_4days_api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty("update_timestamp")
    private String updateTimestamp;
    private String timestamp;
    private List<Forecast> forecasts;

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }
}
