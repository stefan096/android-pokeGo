package com.android.pokemon.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.android.pokemon.dto.UsersPokemonsDTO;
import com.android.pokemon.dto.UsersPokemonsDTOList;
import com.android.pokemon.model.User;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.service.UserService;
import com.android.pokemon.service.UsersPokemonsService;

@Controller
public class UsersPokemonsController {

    @Autowired
    UsersPokemonsService usersPokemonsService;
    @Autowired
    UserService userService;


    @RequestMapping(value = "api/user/{id}/lastPokemonCaught", method = RequestMethod.GET)
    public ResponseEntity<UsersPokemonsDTO> getLastCaught(@PathVariable Long id) {
        UsersPokemons retVal = usersPokemonsService.getLastCaught(id);
        if(retVal == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UsersPokemonsDTO retvalDTO = new UsersPokemonsDTO(retVal);

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "api/usersIdPokemons/{id}", method = RequestMethod.GET)
    public ResponseEntity<UsersPokemonsDTOList> getUsersPokemons(@PathVariable Long id) {
        List<UsersPokemons> retVal = usersPokemonsService.findByUserId(id);
        if(retVal == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Collections.sort(retVal);

        UsersPokemonsDTOList retvalDTO = new UsersPokemonsDTOList(retVal);

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "api/usersPokemon/{id}", method = RequestMethod.GET)
    public ResponseEntity<UsersPokemonsDTO> getPokemons(@PathVariable Long id) {
        UsersPokemons retVal = usersPokemonsService.findById(id);
        UsersPokemonsDTO retvalDTO = new UsersPokemonsDTO(retVal);
        if(retVal == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }


    @RequestMapping(value = "api/cooldown", method = RequestMethod.PUT)
    public ResponseEntity<String> setCooldownPokemon(@RequestParam Long id) {
        usersPokemonsService.setCooldownForPokemon(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "api/populateUserPokemons/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> populateUserPokemons(@PathVariable Long id) {
    	User user = userService.findOne(id);
    	usersPokemonsService.populateUsersPokemon(user);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
