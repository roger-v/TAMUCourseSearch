package application;

public class Room {

    private String location = "";
    private String startTime = "";
    private String endTime = "";
    private String days = "";
    private String seatCapacity = "";
    private String timeLength = "";

    public Room (){

    }

    public Room(String location, String startTime, String endTime, String days, String timeLength, String seatCapacity){
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.seatCapacity = seatCapacity;
        this.timeLength = timeLength;
    }

    public String [] getAll(){
        return new String[]{
                location,
                startTime,
                endTime,
                days,
                timeLength,
                seatCapacity
        };
    }

    public String toString(){
        return (location + ", " + startTime + ", " + endTime + ", " + days + ", " + timeLength + ", " + seatCapacity);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }
}
