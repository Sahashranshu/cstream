package pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class cstreamPojo {

    @SerializedName("cdata")
    private int objectID;
    private long timeStamp;

    public int getObjectID() {
        return objectID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    /*
    Example json data:
    [{'objectId':1, 'timestamp': 9191991}
    {'objectId':2, 'timestamp': 9191992},
    {'objectId':3, 'timestamp': 9191993}]
     */

}
