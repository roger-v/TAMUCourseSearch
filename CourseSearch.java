//CourseSearch.java
package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CourseSearch {
    private String subject = "";
    private String courseNumber  = "";
    private String instructor  = "";

    //Advanced search
    private String courseTitle;
    private String days = "";
    private String startTime  = "";
    private String endTime = "";

    public CourseSearch(){

    }

    public void clearFields(){
        subject = "";
        courseNumber  = "";
        courseTitle  = "";
        instructor  = "";
        days = "";
        startTime  = "";
        endTime = "";
    }

    public void generateResults() throws SQLException{
        Statement stmt = Main.conn.createStatement();
        Main.csTableView.clearTable();
        String sqlc = "select * from Courses1 where";
        int size;
        boolean first = true;
        if(subject.length()!=0){
            first = false;
            size = subject.length();
            String subject2 = (subject.charAt(size-1)=='*') ? subject.substring(0, size-1) : subject;
            sqlc+=" Subj like '"+subject2+"%'";
        }

        if(courseNumber.length()!=0) {
            size = courseNumber.length();
            String no2 = (courseNumber.charAt(size-1)=='*') ? courseNumber.substring(0, size-1) : courseNumber;
            if(!first) sqlc+=" and CourseNo like '"+no2+"%'";
            else {
                sqlc+=" CourseNo like '"+no2+"%'";
                first = false;
            }
        }

        if(courseTitle.length()!=0) {
            size = courseTitle.length();
            String title2 = (courseTitle.charAt(size-1)=='*') ? courseTitle.substring(0, size-1) : courseTitle;
            if(!first) sqlc+=" and Title like '%"+title2+"%'";
            else{
                sqlc+=" Title like '%"+title2+"%'";
                first = false;
            }
        }

        if(instructor.length()!=0) {
            size = instructor.length();
            String ins2 = (instructor.charAt(size-1)=='*') ? instructor.substring(0, size-1) : instructor;
            if(!first) sqlc+=" and Instr like '%"+ins2+"%'";
            else{
                sqlc+=" Instr like '%"+ins2+"%'";
                first = false;
            }
        }
        if(days.length()!=0){
            if(!first) sqlc+=" and Days = '"+days+"'";
            else{
                sqlc+=" Days = '"+days+"'";
                first = false;
            }
        }
        if(startTime.length()!=0){
            if(!first) sqlc+=" and ST >= "+ Main.timeMaker(startTime);
            else{
                sqlc+=" ST >= "+ Main.timeMaker(startTime);
                first = false;
            }
        }
        if(endTime.length()!=0){
            if(!first) sqlc+=" and ET <= "+ Main.timeMaker(endTime);
            else sqlc+=" ET <= "+ Main.timeMaker(endTime);
        }
        if (!Main.canDisplayResults){
            Main.canDisplayResults = true;
            return;
        }

        ResultSet result = stmt.executeQuery(sqlc);
        Main.csTableView.clearTable();

        int i = 0;
        while(result.next()) {
            Course cour = new Course();
            cour.setSubject(result.getString("Subj"));
            cour.setNumber(Integer.toString(result.getInt("courseNo")));
            cour.setInstructor(result.getString("Instr"));
            cour.setTitle(result.getString("Title"));
            cour.setDays(result.getString("Days"));
            cour.setStartTime(Main.timeMaker(result.getInt("ST")));
            cour.setEndTime(Main.timeMaker(result.getInt("ET")));
            cour.setLocation(result.getString("Loc"));
            cour.setCredits(result.getString("Cred"));
            cour.setCrn(result.getString("CRN"));
            cour.setCapacity(result.getString("Cap"));
            cour.setAvailability(result.getString("Av"));
            Main.csTableView.addCourse(cour);
            i++;
        }
        if (i == 0) {
            MessageWindow noResults = new MessageWindow("Course Search", "No results found.");
        }
        //reset scrollbar to the top
        Main.csTableView.courseSearchResults.scrollTo(0);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
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

}
