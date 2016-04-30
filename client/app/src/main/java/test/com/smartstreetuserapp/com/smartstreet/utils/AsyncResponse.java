package test.com.smartstreetuserapp.com.smartstreet.utils;

import java.util.ArrayList;

import test.com.smartstreetuserapp.com.smartstreet.models.Direction;
import test.com.smartstreetuserapp.com.smartstreet.models.NearByPlace;


public interface AsyncResponse {
    void processFinish(ArrayList<NearByPlace> nearByList);
    void processDirectionResult(ArrayList<Direction> directions);
}
