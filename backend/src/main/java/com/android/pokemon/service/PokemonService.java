package com.android.pokemon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Pokemon;
import com.android.pokemon.repository.PokemonRepository;

@Service
public class PokemonService {
	
	@Autowired
	PokemonRepository pokemonRepository;
	
	public List<Pokemon> findAll(){
		return pokemonRepository.findAll();
	}
	
	public Pokemon save(Pokemon pokemon){
		return pokemonRepository.save(pokemon);
	}
	
	public void saveAll(List<Pokemon> pokemons){
		for (Pokemon pokemonToSave : pokemons) {
			pokemonRepository.save(pokemonToSave);
		}
	}

}
