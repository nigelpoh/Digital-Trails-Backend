package types.temperature_realtime_api;

public class Metadata {
    private Station[] stations;
    private String reading_type;
    private String reading_unit;

    public Station[] getStations() {
        return stations;
    }
    public String getReadingType() {
        return reading_type;
    }

    public String getReadingUnit() {
        return reading_unit;
    }
}