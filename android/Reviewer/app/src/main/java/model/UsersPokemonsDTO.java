package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UsersPokemonsDTO implements Serializable {

    @SerializedName("userspokemons")
    @Expose
    private List<UsersPokemons> pokemons;

    public List<UsersPokemons> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<UsersPokemons> pokemons) {
        this.pokemons = pokemons;
    }

}
