package sjsu.cmpe235.smartstreet.user;

import java.util.ArrayList;

import sjsu.cmpe235.smartstreet.user.model.Direction;
import sjsu.cmpe235.smartstreet.user.model.NearByPlace;


public interface NearByInterface {
    void processFinish(ArrayList<NearByPlace> nearByList);
    void processDirectionResult(ArrayList<Direction> directions);
}
