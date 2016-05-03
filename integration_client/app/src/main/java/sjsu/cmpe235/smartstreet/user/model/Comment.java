package sjsu.cmpe235.smartstreet.user.model;

import java.util.Date;

public class Comment {
    private String userName;
    private String comment;
    private float rating;
    private Date date;

    public Comment(String comment, Float rating, String userName, Date date) {
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

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

    public Date getCommentDate() {return date;}

}

