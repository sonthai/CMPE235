package sjsu.cmpe235.smartstreet.user.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sjsu.cmpe235.smartstreet.user.Constant.Constants;

public class Comment {
    private String userName;
    private String comment;
    private float rating;
    private String date;

    public Comment(String comment, float rating, String userName, String date) {
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public Comment() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
}

