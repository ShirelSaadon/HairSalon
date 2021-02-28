package Classes;

import com.google.firebase.database.DatabaseReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Appointment implements  Comparable<Appointment> {
    private String user;
    private String time;
    private String type;
    private String date;
    private String key;


    public Appointment(){}

    public Appointment(String user, String time, String type,String key,String date) {
        this.user = user;
        this.time = time;
        this.type = type;
        this.key = key;
        this.date=date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String  date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        key = key;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getTime().compareTo(o.getTime());
    }
}
