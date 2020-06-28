package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PokeBoss implements Serializable, Comparable<PokeBoss> {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("pokemon")
    @Expose
    private Pokemon pokemon;

    @SerializedName("level")
    @Expose
    private int level;


    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;


    @SerializedName("fightHealt")
    @Expose
    private double fightHealt;


    @SerializedName("imagePath")
    @Expose
    private String imagePath;

    @SerializedName("name")
    @Expose
    private String name;


    @Expose
    private double distance;

    public PokeBoss(Long id, Pokemon pokemon, int level, double latitude, double longitude,
                    double fightHealt, String imagePath, String name) {
        this.id = id;
        this.pokemon = pokemon;
        this.level = level;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fightHealt = fightHealt;
        this.imagePath = imagePath;
        this.name = name;
    }

    public PokeBoss(){}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFightHealt() {
        return fightHealt;
    }

    public void setFightHealt(double fightHealt) {
        this.fightHealt = fightHealt;
    }
    public PokeBoss(Pokemon pokemon){
        this.setPokemon(pokemon);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(PokeBoss o) {
        return (int) Math.round(this.distance - o.getDistance());
    }
}
