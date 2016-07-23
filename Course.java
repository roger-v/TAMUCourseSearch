package application;

public class Course {

    private String subject = "";
    private String number = "";
    private String title = "";
    private String crn = "";
    private String credits = "";
    private String capacity = ""; //number of seats in the room...
    private String availability = ""; //available seats
    private String days = "";
    private String startTime = "";
    private String endTime = "";
    private String instructor = "";
    private String location = "";

    //Subject, Course Number, Course Title, CRN, Credits, Seat Capacity, Seat Availability, Days, Start Time, End Time, Instructor
    public Course(){

    }

    public Course(String subj, String num, String title, String crn, String credits, String cap, String av, String days, String st, String et, String inst, String location){
        this.subject = subj;
        this.number = num;
        this.title = title;
        this.crn = crn;
        this.credits = credits;
        this.capacity = cap;
        this.availability = av;
        this.days = days;
        this.startTime = st;
        this.endTime = et;
        this.instructor = inst;
        this.location = location;
    }

    public String [] getAll(){
        return new String[]{
                subject,
                number,
                title,
                crn,
                credits,
                capacity,
                availability,
                days,
                startTime,
                endTime,
                instructor,
                location
        };
    }

    public void clearCourses(){
        subject = "";
        number = "";
        title = "";
        crn = "";
        credits = "";
        capacity = ""; //number of seats in the room...
        availability = ""; //available seats
        days = "";
        startTime = "";
        endTime = "";
        instructor = "";
        location = "";
    }

    public String toString(){
        String all = "";
        String [] arr = getAll();
        for (String s : arr){
            all += (s + ',');
        }
        all += "\n";
        return all;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
