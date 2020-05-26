package com.android.pokemon.dto;

import com.android.pokemon.model.Pokemon;
import com.android.pokemon.model.UsersPokemons;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@NoArgsConstructor
@Getter
@Setter
public class UsersPokemonsDTO {

    private Long id;
    private Pokemon pokemon;
    private int level;
    private double experience;
    private Instant cooldown;
    
    private double fightHealt;

    public UsersPokemonsDTO(UsersPokemons usersPokemons) {
        this.id = usersPokemons.getId();
        this.pokemon = usersPokemons.getPokemon();
        this.level = usersPokemons.getLevel();
        this.experience = usersPokemons.getExperience();
        this.cooldown = usersPokemons.getCooldown();
        this.fightHealt = usersPokemons.getFightHealt();
    }


    public Instant getCooldown() {
        return cooldown;
    }

    public void setCooldown(Instant cooldown) {
        this.cooldown = cooldown;
    }


	@Override
	public String toString() {
		return "UsersPokemonsDTO [id=" + id + ", pokemon=" + pokemon + ", level=" + level + ", experience=" + experience
				+ ", cooldown=" + cooldown + ", fightHealt=" + fightHealt + "]";
	}


}
