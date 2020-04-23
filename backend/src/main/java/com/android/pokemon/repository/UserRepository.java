package com.android.pokemon.repository;

import com.android.pokemon.model.UsersPokemons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.android.pokemon.model.User;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByEmail(String email);

}
