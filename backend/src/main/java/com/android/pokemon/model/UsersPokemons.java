package com.android.pokemon.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.android.pokemon.enums.FightState;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UsersPokemons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;
    @Enumerated(EnumType.STRING)
    private FightState fightState = FightState.NOT_CHOOSED;
    private int level;
    private double experience;
}
