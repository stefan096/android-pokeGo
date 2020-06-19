package com.android.pokemon.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Boss;
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

    
    
    public UsersPokemons saveCaughtPokemon(Boss caughtBoss, User user) {
    	UsersPokemons newPokemon = new UsersPokemons();
    	newPokemon.setPokemon(caughtBoss.getPokemon());
    	newPokemon.setLevel(caughtBoss.getLevel());
    	newPokemon.setUser(user);
    	newPokemon.setExperience(0);
    	newPokemon.setFightHealt(caughtBoss.getPokemon().getHp());
    	Instant instant = Instant.now();
		Instant value  = instant.plus(3, ChronoUnit.MINUTES);
		newPokemon.setCooldown(value);
    	return usersPokemonsRepository.save(newPokemon);
    }

    public UsersPokemons setCooldownForPokemon(Long id){
    	UsersPokemons newPoke;
		newPoke = usersPokemonsRepository.findById(id).get();
		Instant instant = Instant.now();
		Instant value = instant.plus(2, ChronoUnit.MINUTES);
		newPoke.setCooldown(value);
		return usersPokemonsRepository.save(newPoke);
	}
    
    public UsersPokemons getFirstPokemon(User user) {
    	long[] possibleIds = new long[] {
    		79, // bulbasaur
    		99, // charmander
    		570 // squirtle
    	};

		Random valueGenerator = new Random();
		int randomId = valueGenerator.nextInt(possibleIds.length - 1);
		Optional<Pokemon> pokemon = pokemonRepository.findById(possibleIds[randomId]);
		
		int pokeLevel = valueGenerator.nextInt(20);
		if (pokeLevel < 10) {
			pokeLevel = 10;
		}
		
		UsersPokemons newPokemon = new UsersPokemons();
    	newPokemon.setPokemon(pokemon.get());
    	newPokemon.setLevel(pokeLevel);
    	newPokemon.setUser(user);
    	newPokemon.setExperience(0);
    	newPokemon.setFightHealt(pokemon.get().getHp());
    	
    	return usersPokemonsRepository.save(newPokemon);		
    }
    
    public UsersPokemons getLastCaught(Long id) {
    	List<UsersPokemons> userPokemon = this.findByUserId(id);
    	Comparator<UsersPokemons> byDateComparator = new Comparator<UsersPokemons>() {

			@Override
			public int compare(UsersPokemons o1, UsersPokemons o2) {
				if (o1.getCreatedAt() == null) {
					return 1;
				}
				if (o2.getCreatedAt() == null) {
					return -1;
				}
				return o2.getCreatedAt().compareTo(o1.getCreatedAt());
			}
    	};
    	
    	Collections.sort(userPokemon, byDateComparator);
    	return userPokemon.get(0);
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
