package com.android.pokemon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Boss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ManyToOne
    @JoinColumn(name = "pokemon_id")
    private Pokemon pokemon;
    private int level;
    private double latitude;
    private double longitude;
    
    private double fightHealt;
 
}
