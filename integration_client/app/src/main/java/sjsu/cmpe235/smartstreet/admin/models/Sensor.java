package sjsu.cmpe235.smartstreet.admin.models;

public class Sensor {
    String sensorId;
    String type;
    String date;
    String status;
    String location;

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getSensorId() {

        return sensorId;
    }

    public Sensor(String sensorId, String type, String date, String status, String location) {
        this.sensorId = sensorId;
        this.type = type;
        this.date = date;
        this.status = status;
        this.location = location;
    }

}
