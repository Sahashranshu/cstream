package pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
This class shoudl be dynamic.
 */
public class PojoJsonConverter {

    public static final cstreamPojo[] getcstreamPojoArrayFromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, cstreamPojo[].class);
    }

    public static final cstreamPojo getcstreamPojoFromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, cstreamPojo.class);
    }

    public static final String getJsonFromPojo(Object T){
        String json = new String();
        return json;
    }

    public static final String getJsonArrayFromPojo(Object T){
        String json = new String();
        return json;
    }

}
