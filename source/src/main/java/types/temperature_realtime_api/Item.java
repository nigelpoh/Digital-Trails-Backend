package types.temperature_realtime_api;
import java.util.List;

public class Item {
    private String timestamp;
    private List<Reading> readings;

    public String getTimestamp() {
        return timestamp;
    }
    
    public List<Reading> getReadings() {
        return readings;
    }
}