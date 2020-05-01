package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PokeBossList implements Serializable {

    @SerializedName("pokemonBosses")
    @Expose
    private ArrayList<PokeBoss> pokemonBosses;


    public ArrayList<PokeBoss> getPokemonBosses() {
        return pokemonBosses;
    }

    public void setPokemonBosses(ArrayList<PokeBoss> pokemonBosses) {
        this.pokemonBosses = pokemonBosses;
    }

}
