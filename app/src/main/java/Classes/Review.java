package Classes;

import java.io.File;

public class Review {
    private User user;
    private String command;
    private String type;
    private String date;
    private File imagePath;

    public Review(){}
    public Review(User user, String command, String type, String date) {
        this.user = user;
        this.command = command;
        this.type = type;
        this.date = date;
       // this.imagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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

    public void setDate(String date) {
        this.date = date;
    }

    public File getImagePath() {
        return imagePath;
    }

    public void setImagePath(File imagePath) {
        this.imagePath = imagePath;
    }

}
