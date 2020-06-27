package com.android.pokemon.repository;

import com.android.pokemon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.android.pokemon.model.UsersPokemons;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersPokemonsRepository extends JpaRepository<UsersPokemons, Long>{

    List<UsersPokemons> findByUser(Optional<User> user);
    
    UsersPokemons findByUserIdAndPokemonId(Long userId, Long pokemonId);
}
