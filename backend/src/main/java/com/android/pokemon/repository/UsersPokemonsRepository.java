package com.android.pokemon.repository;

import com.android.pokemon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.android.pokemon.model.UsersPokemons;

import java.util.List;
import java.util.Optional;

public interface UsersPokemonsRepository extends JpaRepository<UsersPokemons, Long>{

    List<UsersPokemons> findByUser(Optional<User> user);
}
