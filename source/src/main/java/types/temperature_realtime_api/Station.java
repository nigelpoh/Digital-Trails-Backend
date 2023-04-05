package types.temperature_realtime_api;

public class Station {
    private String id;
    private String device_id;
    private String name;
    private Location location;

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return device_id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}