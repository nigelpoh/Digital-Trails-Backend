package types.temperature_realtime_api;
import java.util.List;

public class RealtimeWeatherResponse {
  private Metadata metadata;
  private List<Item> items;
  private StatusInfo api_info;

  public Metadata getMetadata() {
    return metadata;
  }

  public List<Item> getItems() {
    return items;
  }

  public StatusInfo getStatusinfo() {
    return api_info;
  }
}