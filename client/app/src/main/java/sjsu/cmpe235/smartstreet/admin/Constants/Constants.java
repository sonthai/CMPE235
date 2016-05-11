package sjsu.cmpe235.smartstreet.admin.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Son Thai on 5/4/2016.
 */
public class Constants {
    final static String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    final static String DATETOSTRING = "MM/dd/yyyy";
    public static final String url = "http://ec2-52-27-135-64.us-west-2.compute.amazonaws.com:3000";

    public static String getDateString(String dateString) {
        try {
            Date date = new SimpleDateFormat(DATEFORMAT).parse(dateString);
            return new SimpleDateFormat(DATETOSTRING).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
