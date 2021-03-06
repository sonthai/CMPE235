package sjsu.cmpe235.smartstreet.user.model;

public class NearByPlace {
    private Location location;
    private String name;
    private String vicinity;
    private Boolean tap;

    public NearByPlace(Location location, String name, String vicinity) {
        this.location = location;
        this.name = name;
        this.vicinity = vicinity;
        this.tap = false;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Boolean getTap() {
        return tap;
    }

    public void setTap(Boolean tap) {
        this.tap = tap;
    }
}
