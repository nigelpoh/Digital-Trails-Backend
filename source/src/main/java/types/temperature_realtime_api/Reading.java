package types.temperature_realtime_api;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reading {
    @JsonProperty("station_id")
    private String stationId;
    private double value;

    public String getStationId() {
        return stationId;
    }

    public double getValue() {
        return value;
    }
}