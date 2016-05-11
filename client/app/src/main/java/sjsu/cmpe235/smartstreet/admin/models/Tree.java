package sjsu.cmpe235.smartstreet.admin.models;

/**
 * Created by Son Thai on 5/4/2016.
 */
public class Tree {
    String treeId;
    String sensorId;
    String date;
    String status;
    String location;

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getTreeId() {
        return treeId;
    }

    public String getSensorId() {

        return sensorId;
    }

    public Tree(String treeId, String sensorId, String date, String status, String location) {
        this.sensorId = sensorId;
        this.treeId = treeId;
        this.date = date;
        this.status = status;
        this.location = location;
    }

}
