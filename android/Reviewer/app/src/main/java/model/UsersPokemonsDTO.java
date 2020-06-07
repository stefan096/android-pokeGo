package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.Instant;

public class UsersPokemonsDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("pokemon")
    @Expose
    private Pokemon pokemon;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("experience")
    @Expose
    private double experience;

    @SerializedName("fightHealt")
    @Expose
    private double fightHealt;

    @SerializedName("cooldown")
    @Expose
    private String cooldown;

    public UsersPokemonsDTO(Long id){
        this.id = id;

    }
    public double getFightHealt() {
        return fightHealt;
    }

    public void setFightHealt(double fightHealt) {
        this.fightHealt = fightHealt;
    }
    public UsersPokemonsDTO(Pokemon pokemon){
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

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }
}
