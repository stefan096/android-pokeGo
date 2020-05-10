package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PokeBoss implements Serializable {

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




    public PokeBoss(){}

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
}
