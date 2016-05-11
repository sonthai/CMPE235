package sjsu.cmpe235.smartstreet.user.model;

public class Direction {
    private String distance;
    private  String duration;
    private String instruction;

    public Direction(String duration, String instruction, String distance) {
        this.duration = duration;
        this.distance = distance;
        this.instruction = instruction;
    }

    public String getDuration() {
        return duration;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDistance() {
        return distance;
    }
}

