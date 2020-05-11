package com.android.pokemon.dto;

import com.android.pokemon.enums.FightStateMove;
import com.android.pokemon.model.Fight;

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
//	private List<UsersPokemonsDTO> pokemons;
	private String timeOfFight;
	private boolean caught;
	private UsersPokemonsDTO pokemonOnMove;
	private FightStateMove fightStateMove;
    private int counterForTurn;
    private int counterForPokemon;
	
	public FightDTO(Fight fight) {
		this.id = fight.getId();
		this.user = fight.getUser() != null ? 
				new UserDTO(fight.getUser()) : null;
		this.boss = fight.getBoss() != null ? 
				new BossDTO(fight.getBoss()) : null;
		this.timeOfFight = fight.getTimeOfFight().toString();
		this.caught = fight.isCaught();
		this.pokemonOnMove = fight.getPokemonOnMove() != null ?
				new UsersPokemonsDTO(fight.getPokemonOnMove()) : null;
		this.fightStateMove = fight.getFightStateMove();
		
//		this.pokemons = new ArrayList<UsersPokemonsDTO>();
//		if(fight.getPokemons() != null) {
//			for (UsersPokemons usersPokemon : fight.getPokemons()) {
//				this.pokemons.add(new UsersPokemonsDTO(usersPokemon));
//			}
//		}
		
		this.counterForPokemon = fight.getCounterForPokemon();
		this.counterForTurn = fight.getCounterForTurn();
	}

	@Override
	public String toString() {
		return "FightDTO [id=" + id + ", user=" + user + ", boss=" + boss + ", timeOfFight=" + timeOfFight + ", caught="
				+ caught + ", pokemonOnMove=" + pokemonOnMove + ", fightStateMove=" + fightStateMove
				+ ", counterForTurn=" + counterForTurn + ", counterForPokemon=" + counterForPokemon + "]";
	}
	
	
}
