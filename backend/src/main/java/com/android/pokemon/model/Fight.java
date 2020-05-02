package com.android.pokemon.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.android.pokemon.enums.FightStateMove;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
    @ManyToOne
    @JoinColumn(name = "boss_id")
	private Boss boss;
//    @OneToMany(mappedBy = "fight")
//	private List<UsersPokemons> pokemons;
	private Instant timeOfFight;
	private boolean caught;
	
    @ManyToOne
    @JoinColumn(name = "users_pokemons_id")
	private UsersPokemons pokemonOnMove;
    @Enumerated(EnumType.STRING)
	private FightStateMove fightStateMove;
    private int counterForTurn;
    private int counterForPokemon;
}
