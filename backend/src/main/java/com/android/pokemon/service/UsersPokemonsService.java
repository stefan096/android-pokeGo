package com.android.pokemon.service;

import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.repository.UserRepository;
import com.android.pokemon.repository.UsersPokemonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersPokemonsService {

    @Autowired
    UsersPokemonsRepository usersPokemonsRepository;
    @Autowired
    UserRepository userRepository;

    public List<UsersPokemons> findByUserId(Long id){
        Optional<User> user = userRepository.findById(id);
        return usersPokemonsRepository.findByUser(user);}
}
