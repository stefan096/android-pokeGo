package com.android.pokemon.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.android.pokemon.model.Pokemon;

import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ListPokemon {
	private Set<UsersPokemons> pokemons = new HashSet<>();
	private Long id;
	private String name;
	private String lastName;
	private String email;
	public ListPokemon(User user){
		this.id= null;
		this.name = null;
		this.lastName = null;
		this.email = null;
		pokemons = user.getPokemons();
	}
}
