package Classes;

import java.util.ArrayList;

public class User {
    private String uid = "";
    private String name = "";
    private String phone = "";
    private ArrayList<Appointment> events;

    public User() { }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }


}
