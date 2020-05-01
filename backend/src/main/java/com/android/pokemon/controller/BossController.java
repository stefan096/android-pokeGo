package com.android.pokemon.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.android.pokemon.dto.BossDTO;
import com.android.pokemon.dto.BossListDTO;
import com.android.pokemon.dto.UsersPokemonsDTO;
import com.android.pokemon.model.Boss;
import com.android.pokemon.model.Pokemon;
import com.android.pokemon.model.UsersPokemons;
import com.android.pokemon.service.PokemonService;

@Controller
public class BossController {

	
	@Autowired
	PokemonService pokemonService;


    @RequestMapping(value = "api/getPokemonsOnMap", method = RequestMethod.GET)
    public ResponseEntity<BossListDTO> getBosses(@RequestParam double lat, @RequestParam double lng) {
        List<Pokemon> retVal = pokemonService.findAll();
        if(retVal == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // TODO : Actually return pokemon bosses that are properly generated
    	Random valueGenerator = new Random();
    	int bossesNearby = valueGenerator.nextInt(8);
        List<Boss> list = new ArrayList<Boss>();
        for (int i = 0; i <= bossesNearby; i++) {
        	Boss boss = new Boss();
        	boss.setPokemon(retVal.get(valueGenerator.nextInt(retVal.size())));
        	boss.setId((long) i);
        	boss.setLevel(valueGenerator.nextInt(100));
        	boss.setLatitude(lat + (valueGenerator.nextDouble()* (valueGenerator.nextBoolean() ? -1 : 1)/100));
        	boss.setLongitude(lng + (valueGenerator.nextDouble()* (valueGenerator.nextBoolean() ? -1 : 1)/100));
        	list.add(boss);
        }
        
        BossListDTO retvalDTO = new BossListDTO(list);

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }
}
