package com.android.pokemon.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Boss;
import com.android.pokemon.model.GeoPoint;
import com.android.pokemon.model.Pokemon;
import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.repository.PokemonRepository;
import com.android.pokemon.repository.UserRepository;
import com.android.pokemon.repository.UsersPokemonsRepository;

@Service
public class UsersPokemonsService {

    @Autowired
    UsersPokemonsRepository usersPokemonsRepository;
    @Autowired
    PokemonRepository pokemonRepository;
    @Autowired
    UserRepository userRepository;

    public UsersPokemons findById(Long id){
       return usersPokemonsRepository.findById(id).get();
    }
    
    public UsersPokemons save(UsersPokemons usersPokemon){
        return usersPokemonsRepository.save(usersPokemon);
     }

    public List<UsersPokemons> findByUserId(Long id){
        Optional<User> user = userRepository.findById(id);
        return usersPokemonsRepository.findByUser(user);}
    
    public void populateUsersPokemon(User user) {
		Random valueGenerator = new Random();
		List<Pokemon> pokemons = pokemonRepository.findAll();
		int howMany = valueGenerator.nextInt(8);
		for (int i = 0; i<= howMany; i++) {
			Pokemon pokemon = pokemons.get(valueGenerator.nextInt(pokemons.size()));
			this.makeOneUserPokemon(user, pokemon, valueGenerator);
		}
    }
    
	public UsersPokemons makeOneUserPokemon(User user, Pokemon pokemon, Random valueGenerator) {
    	UsersPokemons userPokemon = new UsersPokemons();
    	userPokemon.setPokemon(pokemon);
    	userPokemon.setLevel(valueGenerator.nextInt(100));
    	userPokemon.setExperience(valueGenerator.nextDouble());
    	userPokemon.setCooldown(null);
    	userPokemon.setUser(user);
    	
    	userPokemon = usersPokemonsRepository.save(userPokemon);
    	return userPokemon;
	}
}
