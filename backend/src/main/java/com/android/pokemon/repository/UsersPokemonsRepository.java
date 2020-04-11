package com.android.pokemon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.android.pokemon.model.UsersPokemons;

public interface UsersPokemonsRepository extends JpaRepository<UsersPokemons, Long>{

}
