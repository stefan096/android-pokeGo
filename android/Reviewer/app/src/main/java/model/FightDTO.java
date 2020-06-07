package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FightDTO implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("boss")
    @Expose
    private PokeBoss boss;

    @SerializedName("caught")
    @Expose
    private boolean caught;

    @SerializedName("pokemonOnMove")
    @Expose
    private UsersPokemonsDTO pokemonOnMove;

    @SerializedName("fightStateMove")
    @Expose
    private String fightStateMove;

    @SerializedName("counterForTurn")
    @Expose
    private int counterForTurn;

    @SerializedName("counterForPokemon")
    @Expose
    private int counterForPokemon;


    public FightDTO() {}


    public UsersPokemonsDTO getPokemonOnMove() {
        return pokemonOnMove;
    }

    public void setPokemonOnMove(UsersPokemonsDTO pokemonOnMove) {this.pokemonOnMove = pokemonOnMove;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PokeBoss getBoss() {
        return boss;
    }

    public void setBoss(PokeBoss boss) {
        this.boss = boss;
    }

    public boolean isCaught() {
        return caught;
    }

    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    public String getFightStateMove() {
        return fightStateMove;
    }

    public void setFightStateMove(String fightStateMove) {
        this.fightStateMove = fightStateMove;
    }

    public int getCounterForTurn() { return counterForTurn; }

    public void setCounterForTurn(int counterForTurn) {this.counterForTurn = counterForTurn; }

    public int getCounterForPokemon() {return counterForPokemon; }

    public void setCounterForPokemon(int counterForPokemon) {this.counterForPokemon = counterForPokemon;}


}
