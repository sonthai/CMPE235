package test.com.smartstreetuserapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ashutosh on 4/22/2016.
 */
public class userValidate
{
        private static Pattern pattern;
        private static Matcher matcher;
        //Email Pattern
        private static final String EMAIL_PATTERN ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        /**
         * Validate Email with regular expression
         *
         * @param email
         * @return true for Valid Email and false for Invalid Email
         */
        public static boolean validate(String email) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
            return matcher.matches();

        }

        public static boolean isNotNull(String txt){
            return txt!=null && txt.trim().length()>0 ? true: false;
        }
    }

