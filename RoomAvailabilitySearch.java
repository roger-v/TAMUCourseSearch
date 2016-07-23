//RoomAvailabilitySearch.java
package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static application.Main.timeMaker;

public class RoomAvailabilitySearch {
    private String location = "";
    private String startTime = "";
    private String endTime = "";
    private String timeLength = "";
    private String days = "";
    private String seatCapacity = "";

    public RoomAvailabilitySearch(){

    }

    public void clearFields(){
        startTime = "";
        endTime = "";
        timeLength = "";
        days = "";
        seatCapacity = "";
        location = "";
    }

    public void generateResults(){
        //you know the drill
        //use this instance's
        //string data members
        //to create the SQL query
        //then use the results to
        //create Room objects
        //throw them into an array
        //and pass them to Main.raTableView.setRooms(arr);

        //OR use an iterative method where you individually add
        //each course using Main.raTableView.addRoom(room)
        //that way you dont have to use an array
        try{
            Statement stmt = Main.conn.createStatement();

            String loc = location;
            String sc = seatCapacity;
            String tl = timeLength;
            //String days = kb.nextLine(); assuming that the string by theses names are provided within the class
            //String startTime = kb.nextLine();
            //String endTime = kb.nextLine();

            String sqlc = "select * from RASearch where";
            int size;
            boolean first = true;
            if(sc.length()!=0){
                first = false;
                int sc2=Integer.parseInt(sc);
                sqlc+=" Cap >="+sc2;
            }

            if(loc.length()!=0){
                size = loc.length();
                String loc2 = (loc.charAt(size-1)=='*') ? loc.substring(0, size-1) : loc;
                if(!first) sqlc+=" and Loc like '%"+loc2+"%'";
                else {
                    sqlc+=" Loc like '%"+loc2+"%'";
                    first = false;
                }
            }

            if(tl.length()!=0) {
                int tl2 = Integer.parseInt(tl);
                if(!first) sqlc+=" and TL >= "+tl2;
                else{
                    sqlc+=" TL >= "+tl2;
                    first = false;
                }
            }

            if(days.length()!=0) {
                size = days.length();
                if(!first) sqlc+=" and";
                sqlc+=" Days in (";
                for (int i=0; i<size; ++i) {
                    if((days.charAt(i)=='S' || days.charAt(i)=='s')&& i !=size-1){
                        if (days.charAt(i+1)=='U' || days.charAt(i+1)=='u'){
                            sqlc+= "'SU'";
                            ++i;
                        }
                        else
                            sqlc+= "'S'";
                    }
                    else
                        sqlc+="'"+days.charAt(i)+"'";
                    sqlc+= (i!=size-1) ? ", " : ")";
                    //System.out.println(sqlc+"\n");
                }
                first = false;
            }

            if(startTime.length()!=0){
                if(!first) sqlc+=" and";
                	    first = false;
                if(endTime.length()!=0){
                    if(tl.length()!=0){
                        int tl2 = Integer.parseInt(tl);
                        int mustStartBefore = timeMaker(endTime)-tl2;
                        int mustEndAfter = timeMaker(startTime)+tl2;
                        sqlc+=" (ST >= "+ timeMaker(startTime)+" and ST <= "+mustStartBefore+
                                " or ET <= "+ timeMaker(endTime)+" and ET >= "+mustEndAfter+")";
                    }
                    else
                        sqlc+=" (ST < "+ timeMaker(endTime)+" and ET > "+ timeMaker(startTime)+")";
                }
                else{
                    if(tl.length()!=0){
                        int tl2 = Integer.parseInt(tl);
                        int mustEndAfter = timeMaker(startTime)+tl2;
                        sqlc+=" (ST >= "+timeMaker(startTime)+" or ET >= "+mustEndAfter+")";
                    }
                    else
                        sqlc+=" (ET > "+timeMaker(startTime)+")";
                }
            }
            else{
                if(endTime.length()!=0){
                    if(!first) sqlc+=" and";
                    if(tl.length()!=0){
                        int tl2 = Integer.parseInt(tl);
                        int mustStartBefore = timeMaker(endTime)-tl2;
                        sqlc+=" (ET <= "+ timeMaker(endTime)+" or ST <= "+mustStartBefore+")";
                    }
                    else
                        sqlc+=" (ST < "+ timeMaker(endTime)+")";
                }
                else{}
            }
            if (!Main.canDisplayResults){
                Main.canDisplayResults = true;
                return;
            }

            ResultSet result = stmt.executeQuery(sqlc);
            // System.out.println(sqlc);
            Main.raTableView.clearTable();
            int i = 0;
            while(result.next()) {
                Room rom = new Room();
                rom.setLocation(result.getString("Loc"));
                rom.setStartTime(timeMaker(result.getInt("ST")));
                rom.setEndTime(timeMaker(result.getInt("ET")));
                rom.setSeatCapacity(String.valueOf(result.getInt("Cap")));
                rom.setDays(result.getString("Days"));
                rom.setTimeLength(String.valueOf(result.getInt("TL")) + " min.");
                Main.raTableView.addRoom(rom);
                i++;
            }

            if (i == 0){
                MessageWindow noResults = new MessageWindow("Room Availability Search", "No results found.");
            }
            Main.raTableView.roomSearchResults.scrollTo(0);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }
}
