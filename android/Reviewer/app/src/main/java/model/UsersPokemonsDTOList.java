package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UsersPokemonsDTOList implements Serializable {

    @SerializedName("usersPokemonsDTOList")
    @Expose
    private ArrayList<UsersPokemons> usersPokemonsDTOList;


    @SerializedName("usersPokemonsDTO")
    @Expose
    private UsersPokemons usersPokemons;

    public ArrayList<UsersPokemons> getPokemons() {
        return usersPokemonsDTOList;
    }

    public void setPokemons(ArrayList<UsersPokemons> usersPokemonsDTOList) {
        this.usersPokemonsDTOList = usersPokemonsDTOList;
    }
    public UsersPokemons getUsersPokemons() {
        return usersPokemons;
    }

    public void setUsersPokemons(UsersPokemons usersPokemons) {
        this.usersPokemons = usersPokemons;
    }


}
