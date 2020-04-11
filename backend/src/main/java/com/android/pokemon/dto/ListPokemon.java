package com.android.pokemon.dto;

import java.util.List;

import com.android.pokemon.model.Pokemon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ListPokemon {
	private List<Pokemon> pokemons;
}
