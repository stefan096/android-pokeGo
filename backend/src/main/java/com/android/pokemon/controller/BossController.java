package com.android.pokemon.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.android.pokemon.dto.BossDTO;
import com.android.pokemon.dto.BossListDTO;
import com.android.pokemon.dto.GenerateGeoDataDTO;
import com.android.pokemon.model.Boss;
import com.android.pokemon.model.GeoPoint;
import com.android.pokemon.service.BossService;
import com.android.pokemon.service.FightService;
import com.android.pokemon.service.PokemonService;
import com.android.pokemon.service.UsersPokemonsService;
import com.android.pokemon.utils.HelpersGeo;

@Controller
public class BossController {

	
	@Autowired
	PokemonService pokemonService;
	
	@Autowired
	UsersPokemonsService usersPokemonsService;
	
	@Autowired
	FightService fightService;
	
	@Autowired
	BossService bossService;
	
	@Autowired
	HelpersGeo HelpersGeo;

    @RequestMapping(value = "api/boss", method = RequestMethod.GET)
    public ResponseEntity<BossDTO> getBoss(@RequestParam Long id) {
        Boss boss = bossService.findById(id);
        BossDTO bossDTO = new BossDTO(boss);
        if(boss == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(bossDTO, HttpStatus.OK);
        }
    }

	/**
	 * 
	 * @return Bosses on whole map
	 */
    @RequestMapping(value = "api/getPokemonsOnMap", method = RequestMethod.GET)
    public ResponseEntity<BossListDTO> getBosses(@RequestParam double lat, @RequestParam double lng) {
   	
        List<Boss> list = new ArrayList<Boss>();
        list = bossService.findAll();
        BossListDTO retvalDTO = new BossListDTO(list);

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }
    
	/**
     * @param dataDTO
     * @param dataDTO.geopoint.latitude - starting point latitude
     * @param dataDTO.geopoint.longitude - starting point longitude
     * @param dataDTO.radius - radius to look up pokemons
	 * @return Bosses on specified starting point and radius
	 */
    @RequestMapping(value = "api/getPokemonsOnMap/specific", method = RequestMethod.PUT)
    public ResponseEntity<BossListDTO> getBosses(@RequestBody GenerateGeoDataDTO dataDTO) {
    	
    	System.out.println("lat: " + dataDTO.getGeopoint().getLatitude());
    	System.out.println("long: " + dataDTO.getGeopoint().getLongitude());
    	System.out.println("radius: " + dataDTO.getRadius());
   	
        List<Boss> list = new ArrayList<Boss>();
        List<Boss> retVal = new ArrayList<Boss>();
        list = bossService.findAll();
        
        for (Boss boss : list) {
        	GeoPoint geopoint = new GeoPoint(boss.getLatitude(), boss.getLongitude());
        	double distance = HelpersGeo.distanceBetweenTwoGeopoints(dataDTO.getGeopoint(), geopoint);
        	System.out.println("distance: " + distance);
			if(distance < dataDTO.getRadius()) {
				retVal.add(boss);
			}
		}
        
        BossListDTO retvalDTO = new BossListDTO(retVal);

        return new ResponseEntity<>(retvalDTO, HttpStatus.OK);
    }
    
    /**
     * Delete previously generated data and generate new data
     * @param dataDTO
     * @param dataDTO.numberOfData - number of data to generate
     * @param dataDTO.geopoint.latitude - starting point latitude
     * @param dataDTO.geopoint.longitude - starting point longitude
     * @param dataDTO.radius - radius to generate pokemons
     */
    /*
     *JSON novi sad - example
		{
			"numberOfData": 100, 
			"geopoint": {
				"latitude" : 45.267136,
				"longitude": 19.833549
			},
			"radius" : 10000
		}
     */
    @RequestMapping(value = "api/data", method = RequestMethod.POST)
    public ResponseEntity<BossListDTO> getData(@RequestBody GenerateGeoDataDTO dataDTO) {
    	//moram obrisati sve borbe zbog referenciranja boss-ova
    	fightService.deleteAll();
    	bossService.deleteAll();
    	HelpersGeo.randomGeneratePokemonsCoordinates(dataDTO.getNumberOfData(), dataDTO.getGeopoint(), dataDTO.getRadius());

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
