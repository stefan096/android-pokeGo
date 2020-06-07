package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GenerateGeoDataDTO implements Serializable {

    @SerializedName("numberOfData")
    @Expose
    private int numberOfData;

    @SerializedName("geopoint")
    @Expose
    private Geopoint geopoint; //start point

    @SerializedName("radius")
    @Expose
    private double radius; //in metres

    public GenerateGeoDataDTO(){

    }

    public int getNumberOfData() {
        return numberOfData;
    }

    public void setNumberOfData(int numberOfData) {
        this.numberOfData = numberOfData;
    }

    public Geopoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(Geopoint geopoint) {
        this.geopoint = geopoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}