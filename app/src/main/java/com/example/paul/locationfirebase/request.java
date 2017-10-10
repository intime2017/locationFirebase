package com.example.paul.locationfirebase;

/**
 * Created by Paul on 10/1/2017.
 */

public class request {
    String rID,lat1,lat2,lon1,lon2;

    public request()
    {

    }

    public request(String ID, String lat1, String lat2, String lon1, String lon2) {
        this.rID = ID;
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lon1 = lon1;
        this.lon2 = lon2;
    }

    public String getID() {
        return rID;
    }

    public String getLat1() {
        return lat1;
    }

    public String getLat2() {
        return lat2;
    }

    public String getLon1() {
        return lon1;
    }

    public String getLon2() {
        return lon2;
    }
}
