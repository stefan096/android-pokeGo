package com.android.pokemon.controller;

import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.service.UserService;
import com.android.pokemon.service.UsersPokemonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class UsersPokemonsController {

    @Autowired
    UsersPokemonsService usersPokemonsService;
    @Autowired
    UserService userService;

   /* @RequestMapping(value = "api/userspokemons/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<UsersPokemons>> getUsersPokemons(@PathVariable Long id) {

        User user = userService.findOne(id);

        if(pokemons == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(pokemons,HttpStatus.OK);
    }*/
}