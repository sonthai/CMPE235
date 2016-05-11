package test.com.smartstreetuserapp.com.smartstreet.models;

/**
 * Created by Ashutosh on 5/1/2016.
 */
public class CommentModel {
    private String userName;
    private String comment;
    private float rating;

    public CommentModel() {
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


}

