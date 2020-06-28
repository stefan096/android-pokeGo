package com.android.pokemon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.android.pokemon.model.Boss;
import com.android.pokemon.model.GeoPoint;
import com.android.pokemon.model.Pokemon;
import com.android.pokemon.repository.BossRepository;
import com.android.pokemon.repository.PokemonRepository;

@Service
public class HelpersGeo {
	
	@Autowired 
	BossRepository bossRepository;
	
	@Autowired
	PokemonRepository pokemonRepository;
	
	public GeoPoint generateRandomCoordinates(GeoPoint geopoint, double radius) {
	    double y0 = geopoint.getLatitude();
	    double x0 = geopoint.getLongitude();
	    double rd = radius / 111300;

	    double u = Math.random();
	    double v = Math.random();

	    double w = rd * Math.sqrt(u);
	    double t = 2 * Math.PI * v;
	    double x = w * Math.cos(t);
	    double y = w * Math.sin(t);

	    //double xp = x / Math.cos(y0); //odalji ga od zadatog radijusa ali resava problem sa istokom i zapadom, 
	    //no nevazno za tacnost
	    
	    GeoPoint generatedGeopoint = new GeoPoint();
	    generatedGeopoint.setLatitude(y + y0);
	    generatedGeopoint.setLongitude(x + x0);  //xp + x0

	    return generatedGeopoint;
	}
	
	public double distanceBetweenTwoGeopoints(GeoPoint geopoint1, GeoPoint geopoint2) {
		double lat1 = geopoint1.getLatitude();
		double lon1 = geopoint1.getLongitude(); 
		double lat2 = geopoint2.getLatitude();
		double lon2 = geopoint2.getLongitude();
		

		
	    double R = 6371000;
	    double a = 0.5 - Math.cos((lat2 - lat1) * Math.PI / 180) / 2 + Math.cos(lat1 * Math.PI / 180) 
	    * Math.cos(lat2 * Math.PI / 180) * (1 - Math.cos((lon2 - lon1) * Math.PI / 180)) / 2;
	    
	    return R * 2 * Math.asin(Math.sqrt(a));
	}
	
	public List<Boss> randomGeneratePokemonsCoordinates(int numberOfData, GeoPoint geopoint, double radius) {
		
		Random valueGenerator = new Random();
		List<Boss> listBosses = new ArrayList<Boss>();
		List<Pokemon> pokemons = pokemonRepository.findAll();
		
		for(int i = 0; i < numberOfData; ++i) {
			Pokemon pokemon = pokemons.get(valueGenerator.nextInt(pokemons.size()));
			GeoPoint generated = generateRandomCoordinates(geopoint, radius);
			double distance = distanceBetweenTwoGeopoints(geopoint, generated);
			writeData(generated, distance);
			
			Boss boss = this.makeOneBoss(generated, pokemon, valueGenerator);
        	listBosses.add(boss);
		}
		
		return listBosses;
	}
	
	public Boss makeOneBoss(GeoPoint generated, Pokemon pokemon, Random valueGenerator) {
    	Boss boss = new Boss();
    	boss.setPokemon(pokemon);
    	boss.setLevel(generateLevelWithProbability(7, 30)); //70% to 30 lvl
    	boss.setLatitude(generated.getLatitude());
    	boss.setLongitude(generated.getLongitude());
    	
    	Boss savedBoss = bossRepository.save(boss);
    	return savedBoss;
	}
	
	public void writeData(GeoPoint geopoint, double distance) {
		System.out.println("lat: " + geopoint.getLatitude());
		System.out.println("lng: " + geopoint.getLongitude());
		System.out.println("distance: " + distance);
		System.out.println("------------------------------------------------------------------------------------");
	}
	
	/**
	 * scheduler with initial delay and fixed after that, update pokemon and level on boss
	 */
	@Scheduled(fixedDelay = 1000*60*100, initialDelay = 1000)
	public void scheduleFixedRateWithInitialDelayTask() {
		Random valueGenerator = new Random();
	  
		List<Boss> bosses = bossRepository.findAll();
		List<Pokemon> pokemons = pokemonRepository.findAll();
		
		if(bosses.size() == 0 || pokemons.size() == 0) {
			return;
		}

		int paramToUpdate = valueGenerator.nextInt(bosses.size());
		
		for(int i = 0; i < bosses.size(); ++i) {
			
			if((i+1) % paramToUpdate == 0) {
				bosses.get(i).setLevel(valueGenerator.nextInt(100));
				Pokemon pokemon = pokemons.get(valueGenerator.nextInt(pokemons.size()));
				bosses.get(i).setPokemon(pokemon);
				//moze se dodati/oduzeti neki faktor od koordinata ali nema potrebe za tim
				
				Boss savedBoss = bossRepository.save(bosses.get(i));
				System.out.println("updated: "  + savedBoss.getId());
			}
		}
	}
	
	private int generateLevelWithProbability(int probabilility, int border){ //probabilility from 0 to 9, border for level
	    Random random = new Random();
	    int val = random.nextInt(10);

	    if (val < probabilility) {
	        return random.nextInt(border) + 1;      // random range 1 to 30
	    }
	    else {
	        return random.nextInt(border + 1) + 70;     // random range 30 to 100
	    }
	}
	
	//to change one specific boss
	public void changeOneSpecificBoss(Long id) {
		Random valueGenerator = new Random();
	  
		Optional<Boss> optionalBoss = bossRepository.findById(id);
		Boss boss = optionalBoss.get();
		List<Pokemon> pokemons = pokemonRepository.findAll();
		
		boss.setLevel(valueGenerator.nextInt(100));
		Pokemon pokemon = pokemons.get(valueGenerator.nextInt(pokemons.size()));
		boss.setPokemon(pokemon);
		
		Boss savedBoss = bossRepository.save(boss);
		System.out.println(" ONE updated: "  + savedBoss.getId());
	}

}
