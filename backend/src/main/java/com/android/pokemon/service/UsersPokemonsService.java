package com.android.pokemon.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.repository.UserRepository;
import com.android.pokemon.repository.UsersPokemonsRepository;

@Service
public class UsersPokemonsService {

    @Autowired
    UsersPokemonsRepository usersPokemonsRepository;
    @Autowired
    UserRepository userRepository;

    public UsersPokemons findById(Long id){
       return usersPokemonsRepository.findById(id).get();
    }

    public List<UsersPokemons> findByUserId(Long id){
        Optional<User> user = userRepository.findById(id);
        return usersPokemonsRepository.findByUser(user);}
}
