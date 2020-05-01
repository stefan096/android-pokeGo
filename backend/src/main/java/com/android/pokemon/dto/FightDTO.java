package com.android.pokemon.dto;

import java.util.ArrayList;
import java.util.List;

import com.android.pokemon.enums.FightStateMove;
import com.android.pokemon.model.Fight;
import com.android.pokemon.model.UsersPokemons;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FightDTO {
	private Long id;
	private UserDTO user;
	private BossDTO boss;
	private List<UsersPokemonsDTO> pokemons;
	private String timeOfFight;
	private boolean caught;
	private UsersPokemonsDTO pokemonOnMove;
	private FightStateMove fightStateMove;
	
	public FightDTO(Fight fight) {
		this.id = fight.getId();
		this.user = new UserDTO(fight.getUser());
		this.boss = new BossDTO(fight.getBoss());
		this.timeOfFight = fight.getTimeOfFight().toString();
		this.caught = fight.isCaught();
		this.pokemonOnMove = new UsersPokemonsDTO(fight.getPokemonOnMove());
		this.fightStateMove = fight.getFightStateMove();
		
		this.pokemons = new ArrayList<UsersPokemonsDTO>();
		if(fight.getPokemons() != null) {
			for (UsersPokemons usersPokemon : fight.getPokemons()) {
				this.pokemons.add(new UsersPokemonsDTO(usersPokemon));
			}
		}
	}
}
